package com.mygdx.game.levels;

import com.badlogic.gdx.math.Vector2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Dell on 27-03-2016.
 */
public class level1 {
    public Vector2[][] lines;
    public ArrayList<Vector2> text;
    public ArrayList<ArrayList<Vector2>> body;
    //public int[][]={{}};

   // public Vector2[][] makestage(){
    //    for(int i=0;i<points.)
        //return lines;
   // }
    public static void main(String args[]) throws Exception {
        new level1().readJson();
    }
   void readJson()throws Exception{
       JSONParser parser = new JSONParser();
       Object obj = parser.parse(new FileReader("C:\\Users\\Dell\\Documents\\GitHub\\psy-physics\\core\\src\\com\\mygdx\\game\\levels\\level.json"));
       text=new ArrayList<Vector2>();
       body=new ArrayList<ArrayList<Vector2>>();
       JSONObject jsonObject = (JSONObject) obj;

       JSONArray texture = (JSONArray) jsonObject.get("Texture");
       Iterator iterator = texture.iterator();
       while (iterator.hasNext()) {
           String tp=(String)iterator.next();
          // System.out.println(tp.substring(1,tp.indexOf(','))+" "+tp.substring(tp.indexOf(','),tp.indexOf(')')));

           float x=Float.parseFloat(tp.substring(1,tp.indexOf(',')));
           float y=Float.parseFloat(tp.substring(tp.indexOf(',')+1,tp.indexOf(')')));
           text.add(new Vector2(x,y));

       }
       System.out.println("Tecture "+Arrays.toString(text.toArray()));

       texture = (JSONArray) jsonObject.get("Points");
       JSONObject jb= (JSONObject) texture.get(0);


           int i = 0;
           //System.out.println("Here");
           while (jb.get("" + i + "")!=null) {
               String tmp= (String) jb.get("" + i + "");
                int j=tmp.indexOf("(");
               ArrayList<Vector2> tmps=new ArrayList<Vector2>();
               try {
                   while (j != -1) {
                       String tp = tmp.substring(j, tmp.indexOf(' ', j) - 1);
                       j = tmp.indexOf(' ', j);
                       //System.out.println(tp);
                       // System.out.println(tp.substring(1,tp.indexOf(','))+" "+tp.substring(tp.indexOf(','),tp.indexOf(')')));

                       float x = Float.parseFloat(tp.substring(1, tp.indexOf(',')));
                       float y = Float.parseFloat(tp.substring(tp.indexOf(',') + 1, tp.indexOf(')')));
                       tmps.add(new Vector2(x, y));
                       //System.out.println(j);

                       j = tmp.indexOf('(', j);
                      // System.out.println(j);

                   }
               }
               catch (Exception e){
                   String tp = tmp.substring(j, tmp.indexOf(']', j));
                   j = tmp.indexOf(']', j);
                  // System.out.println(tp);
                   // System.out.println(tp.substring(1,tp.indexOf(','))+" "+tp.substring(tp.indexOf(','),tp.indexOf(')')));

                   float x = Float.parseFloat(tp.substring(1, tp.indexOf(',')));
                   float y = Float.parseFloat(tp.substring(tp.indexOf(',') + 1, tp.indexOf(')')));
                   tmps.add(new Vector2(x, y));
                   //System.out.println(j);

                   //j = tmp.indexOf('(', j);
                   //System.out.println(j);
               }
               body.add(tmps);
               System.out.println("List "+Arrays.toString(tmps.toArray()));
              // System.out.println(jb.get("" + i + ""));
               i++;
           }



   }

}
