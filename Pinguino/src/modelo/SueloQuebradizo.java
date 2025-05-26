// Source code is decompiled from a .class file using FernFlower decompiler.
package modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class SueloQuebradizo extends Casilla {
   public SueloQuebradizo(int posicion, ArrayList<Jugador> jugadoresActuales) {
      super(posicion, jugadoresActuales);
   }

   public void realizarAccion() {
      Iterator var2 = this.jugadoresActuales.iterator();

      while(true) {
         Jugador j;
         do {
            do {
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  j = (Jugador)var2.next();
               } while(!(j instanceof Pinguino));
            } while(((Pinguino)j).getInv().getLista().size() == 0);
         } while(((Pinguino)j).getInv().getLista().size() > 0 && ((Pinguino)j).getInv().getLista().size() < 5);

         ((Pinguino)j).getInv().getLista().size();
      }
   }
}
