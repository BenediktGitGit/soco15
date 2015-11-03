package org.neuroph;

import com.opencsv.CSVReader; //Erlaubt einfache Einbindung von CSV-Dateien.
import java.io.FileNotFoundException; //Exception wird geworfen, fall keine CSV gefunden wurde.
import java.io.FileReader; //Benötigt zum Einlesen der CSV.
import java.io.IOException; //Exception wird geworfen, falls es Probleme beim Einlesen der CSV ergeben.
import java.lang.reflect.Array;
import java.util.Arrays; //Zum Erstellen von Arrays.
import java.util.LinkedList; //Zum Erstellen von verketteten Listen.
import org.neuroph.core.data.DataSet; //Benötigt, um Datensätze anzulegen.
import org.neuroph.core.NeuralNetwork; //Benötigt, um ein neuronales Netz zu erstellen.
import org.neuroph.core.data.DataSetRow; //Benötigt, um Zeilen im Datensatz anzulegen.
import org.neuroph.nnet.MultiLayerPerceptron; //Benötigt, um ein MLP zu erstellen.

public class Main 
{
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {            
        //Lade ein in Neuroph bereits trainiertes und getestetes neuronales Netz.
        NeuralNetwork ann = MultiLayerPerceptron.createFromFile("ann/4-09-1_Sigmoid_Bias.nnet"); //Namenskonvention: input-hidden-output_transferfunction   
        
        //Lade einen  normalisierten Datensatz, über dessen Zeitraum vorhergesagt wird.
        //Mit diesem Wert wurde der Datensatz normalisiert.
         int normfactor = 10000;
         DataSet PredictionSet = new DataSet(4,1); 
         CSVReader reader = new CSVReader(new FileReader("predict/31.03.2009_31.03.2009-PredictionSet.csv")); //Namenskonvention: Startdatum_Enddatum-Set
                 
         //Verkettete Liste, die das Datum der Börsenkurse speichert.
         LinkedList datelist = new LinkedList();
        
         //Dient als Cache für die aktuelle Zeile
         String [] nextLine;
         
         while ((nextLine = reader.readNext()) != null) 
         {
            //Setze den Input sowie den tatsächlichen Output (der tatsächliche Börsenkurs). 
            PredictionSet.addRow(new DataSetRow(new double[]{Double.parseDouble(nextLine[1]),
                Double.parseDouble(nextLine[2]),Double.parseDouble(nextLine[3]),Double.parseDouble(nextLine[4])}, new double[]{Double.parseDouble(nextLine[5])}));
        
            //Setze Datum in die verketteten Listen.
            datelist.add(nextLine[0]); 
         }
    
        //Die benötigten Variablen zur Ausgabe werden definiert und initialisiert.
        double MSE = 0.0D;  //Zur Berrechnung des MSE des trainierten KNN.
        int numoverall = 0; //Die Anzahl der bisher verarbeiteten Börsentage.
        int numtoolow = 0;  //Die Anzahl der zu niedrigen Vorhersagen (Abweichung < -20).
        int numright = 0;   //Die Anzahl der richtigen Vorhersagen (Abweichung maximal +/- 20).
        int numtoohigh = 0;  //Die Anzahl der zu hohen Vorhersagen (Abweichung > 20).
         
        //Über die einzelnen Zeilen des Datensatze iterieren.
        for (DataSetRow row : PredictionSet.getRows())
        {
            //Neuer Zeile wird gelesen, also Anzahl verarbeiter Zeilen des Datensatzes + 1.
            numoverall = numoverall +1;
            //Lese eine Zeile in das KNN und berechne den Output.
            ann.setInput(row.getInput());
            ann.calculate();
          
            //Die Werte müssen nun denormalisiert werden.
            //Denormalisiere den Output des Ausgabeneurons
            double denormoutput = Array.getDouble(ann.getOutput(),0)*normfactor;
            //Denormalisiere den gewünschten Output,
            double denormdesiredoutput = Array.getDouble(row.getDesiredOutput(),0)*normfactor;

            //Denormalisiere den Input
            double denorminput [] = row.getInput();
            for(int i = 0; i < denorminput.length;i++)
                denorminput[i] = denorminput[i]*normfactor;
            
            //Kalkuliere Delta und MSE
            double delta = (denormoutput - denormdesiredoutput);
            MSE = MSE + Math.pow(delta,2)/numoverall; 
            
            //Kalkuliere Vorhersagegüte
            if(delta < -0.0020)
                numtoolow = numtoolow+1;
            else if(delta > 0.0020)
                numtoohigh = numtoohigh+1;
            else 
                numright = numright +1;
                     
            //Ausgabe: Der Tag|Die 4 Inputneuronen||Das Outputneuron||Der tatsächliche Wert||Der MSE||Anzahl zu hoher, richtiger und zu niedriger Vorhesagen.
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Betrachter Börsenschluss am: " + datelist.pop() + " Inputneuronen: " + Arrays.toString(denorminput)+ " Outputneuron: " + denormoutput);   
            System.out.println(" Tatsächlicher Wert: " + denormdesiredoutput);
            System.out.print("Abweichung vom tatsächlichen Börsenkurs: " + delta + " Mean Squared Error: " + MSE);
            System.out.println(" Anzahl zu niedriger Vorhersagen: "+numtoolow+" Anzahl richtiger Vorhersagen: " + numright + " Anzahl zu hoher Vorhersagen: " + numtoohigh);      
        }
     
      //Beende das Programm  
      System.exit(0);
    }
}


