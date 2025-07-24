package Principal;
//Importar java swing JFrame

import Juego.PanelJuego;

import javax.swing.*;

public class SnakeGame extends JFrame {

    public SnakeGame() {//Contructor
        setTitle("Juego-Laura");
        setSize(600, 600);
        //Para que el usuario no pueda cambiar el tamaño de la pantalla
        setResizable(false);
        //cierra la ventana cuando se cierra el juego
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Centra la pantalla
        setLocationRelativeTo(null);
        /*setVisible no se pone aqui para que no
        visualice automaticamente sin darle tiempo a cargar el juego*/


        //añadir el panel del juego

        PanelJuego panel = new PanelJuego();
        add(panel);                  // Añadimos el panel de juego
        addKeyListener(panel);       // Escuchamos el teclado desde el panel
        setFocusable(true);          // Para que reciba eventos del teclado
        requestFocus();              // Aseguramos el foco en la ventana
        pack();                      // Ajustamos el tamaño de la ventana al contenido
    }

    public static void main(String[] args) {
        SnakeGame juego = new SnakeGame();//instancia de juego
        //Aqui añadimos cosas y luego se visualiza el juego


        //Para que el juego sea visible
        juego.setVisible(true);

    }
}
