
package org.neuroph;

import com.opencsv.CSVReader; //Erlaubt einfache Einbindung von CSV-Dateien.
import java.io.FileNotFoundException; //Exception wird geworfen, fall keine CSV gefunden wurde.
import java.io.FileReader; //Benötigt zum Einlesen der CSV
import java.io.IOException; //Exception wird geworfen, fall es Probleme beim Einlesen der CSV ergeben.
import java.util.Arrays;
import java.util.LinkedList;
import org.neuroph.core.data.DataSet; //Benötigt, um Datensätze anzulegen.
import org.neuroph.core.NeuralNetwork; //Benötigt, um ein neuronales Netz zu erstellen.
import org.neuroph.core.data.DataSetRow; //Benötigt, um Zeilen im Datensatz anzulegen.
import org.neuroph.nnet.MultiLayerPerceptron; //Benötigt, um ein MLP zu erstellen.

public class Main 
{
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {            
        //Lade ein in Neuroph bereits trainiertes und getestetes neuronales Netz.
        NeuralNetwork knn = MultiLayerPerceptron.createFromFile("ann/4-09-1_Sigmoid.nnet");
        //Lade einen Datensatz, über dessen Zeitraum vorhergesagt wird.
        DataSet PredictionSet = new DataSet(4,1);
        //Parse die CSV in zwei Arrays rein: datearr[] enthält alle Daten als double daxarr[] enthält die DAX-Werte von t-4 bis t         
         CSVReader reader = new CSVReader(new FileReader("predict/31-03-2009_31.03.2009-PredictionSet.csv"));
         String [] nextLine;
         
         //Zwei verkettete Listen: Eine für das Datum|| Eine für den echten Backpropagationwert.
         LinkedList datelist = new LinkedList();
         LinkedList backproplist = new LinkedList();
        
         while ((nextLine = reader.readNext()) != null) 
         {
            //Setze Input sowie tatsächlichen Output 
            System.out.println(nextLine[0]+ nextLine[1] + nextLine[2] + nextLine[3] + nextLine[4] + nextLine[5]);
            PredictionSet.addRow(new DataSetRow(new double[]{Double.parseDouble(nextLine[1]),
            Double.parseDouble(nextLine[2]),Double.parseDouble(nextLine[3]),Double.parseDouble(nextLine[4])}, new double[]{Double.parseDouble(nextLine[5])}));
        
            //Setze Datum und Backpropagationwert in verkettete Liste.
            datelist.add(nextLine[0]);
            backproplist.add(nextLine[5]);
            
         }
    
        //Die benötigten Variablen vordefinieren.
        double MSE = 0.0D;  //Der MSE
        int numoverall = 0; //Die Anzahl der bisher verarbeiteten Zeilen des Datensatzes.
        int numtoolow = 0;   //Die Anzahl der zu niedrigen Vorhersagen (Abweichung > 2)
        int numright = 0;   //Die Anzahl der zu richtigen Vorhersagen (Abweichung maximal +/- 2)
        int numtoohigh = 0;  //Die Anzahl der zu hohen Vorhersagen  (Abweichung > 2)
         
        //Über die einzelnen Zeilen des Datensatze iterieren.
        for (DataSetRow row : PredictionSet.getRows())
        {
            //Neuer Zeile wird gelesen, also Anzahl verarbeiter Zeilen des Datensatzes + 1
            numoverall = numoverall +1;
            //Lese Zeile in das knn und berechne Output
            knn.setInput(row.getInput());
            knn.calculate();
         
            //Hier wird Ausgegeben: Der Tag||Die 4 Inputneuronen||Das Outputneuron||Der tatsächliche Wert||Der MSE
            //Sowie die Anzahl zu hoher,perfekter und zu niedriger Vorhesagen.
            double[ ] networkOutput = knn.getOutput();
            System.out.print("Betrachter Börsenschluss am: " + datelist.pop());
            
            System.out.print(" Inputneuronen: " + Arrays.toString(row.getInput()));
            System.out.print(" Outputneuron: " + Arrays.toString(networkOutput)); 
            
            double realval = Double.parseDouble((String) backproplist.pop());
            System.out.print(" Tatsächlicher Wert: " + realval);
            double delta = realval - networkOutput[0];
            MSE = MSE + Math.pow(delta,2)/numoverall;
            System.out.print(" MSE: " + MSE);
            
            if(delta < -0.0010)
                numtoolow = numtoolow+1;
            else if(delta > 0.0010)
                numtoohigh = numtoohigh+1;
            else 
                numright = numright +1;
            
            System.out.println("Anzahl: Zu hohe : "+numtoolow+" Richtige: " + numright + " Zu niedrige: " + numtoohigh+ " Vorhersgen.");
        }
        
      //Beende das Programm  
      System.exit(0);
    }
}


