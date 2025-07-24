package Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
/*
awt significa Abstract Window Toolkit
Es una biblioteca de Java para crear interfaces gráficas de usuario (GUI).
*/

//Vamos a crear el panel del juego hereda de JPanel:
public class PanelJuego extends JPanel implements KeyListener {

    //Este es el tamaño real del área del juego(constante)
    public static final int ANCHO = 600;
    public static final int ALTO = 600;
    //variables que controlan la posición de la serpiente
    private LinkedList<Point> cuerpoSerpiente;
    private int direccionX = 20;
    private int direccionY = 0;
    private Point comida;
    private int puntos = 0;
    private Timer timer; // variable de instancia
    private int velocidad = 100; //delay(velocidad) iniciar
    // Lista para almacenar las trampas
    private LinkedList<Point> trampas;
    // Número inicial de trampas que quieres en el juego
    private int nivel = 1;//Nivel inicial
    private int trampasPorNivel = 2; //Num inicial de trampas


    public PanelJuego() { //Constructor
        cuerpoSerpiente = new LinkedList<>();
        cuerpoSerpiente.add(new Point(100, 100));
        trampas = generarTrampas(trampasPorNivel);
        comida = generarComida();


        /*
        Usamos setPreferredSize()
        en lugar de setSize() porque más adelante usaremos
        pack() en la ventana.
        pack() ajusta el JFrame al tamaño del panel ejemplo ventana.pack()"600,600"*/
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setBackground(Color.BLACK); // Fondo del panel
        //Ahora tenemos que añadir un temporizador:

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point cabezaActual = cuerpoSerpiente.getFirst();
                Point nuevaCabeza = new Point(cabezaActual.x + direccionX, cabezaActual.y + direccionY);
                cuerpoSerpiente.addFirst(nuevaCabeza);
                // Comprobar colisión con trampas
                for (int i = 0; i < trampas.size(); i++) {
                    Point t = trampas.get(i);
                    if (nuevaCabeza.equals(t)) {
                        ((Timer) e.getSource()).stop();
                        JOptionPane.showMessageDialog(null, "¡Game Over! Te has chocado con una trampa.");
                        reiniciarJuego();
                        return;
                    }
                }
                //Detectamos colisiones con el cuerpo (excluyendo la nueva cabeza)
                //El indice 0 es la cabeza por eso empezamos en 1
                for (int i = 1; i < cuerpoSerpiente.size(); i++) {
                    //Comprobamos si la nueva posición de la cabeza e igual a la posicion de cualquier segmento del cuerpo
                    if (nuevaCabeza.equals(cuerpoSerpiente.get(i))) {
                        //con cuerpoSerpiente.get(i) accedemos a cada parte del cuerpo una por una.
                        //Point p = cuerpoSerpiente.get(1);(devuelve el elemento de la posicion i de la lista)
                        ((Timer) e.getSource()).stop();//Detenemos el juego
                        JOptionPane.showMessageDialog(null, "¡Game Over! Te has chocado contigo mismo.");
                        reiniciarJuego();
                        return;


                    }
                }

                //Comprobar si la serpiente come la comida:
                if (nuevaCabeza.equals(comida)) {
                    puntos++;
                    //La serpiente come la comida: no eliminamos la última parte para que crezca
                    comida = generarComida(); //generamos nueva comida en otra posición
                    actualizarNivel();
                    //Aumenta velocidad, pero sin pararse
                    if (velocidad > 30) {//Limite mínimo de velocidad (30ms)
                        velocidad -= 5; //reduce delay para que vaya más rápido
                        timer.setDelay(velocidad);
                    }

                } else {
                    //La serpiente se mueve sin crecer , quitamos la ultima parte
                    cuerpoSerpiente.removeLast();
                }

                //Comprobamos los límites para que no se salga de la ventana
                if (nuevaCabeza.x < 0 || nuevaCabeza.x >= ANCHO || nuevaCabeza.y < 0 || nuevaCabeza.y >= ALTO) {
                    ((Timer) e.getSource()).stop();//Detenemos el juego
                    JOptionPane.showMessageDialog(null, "¡Game Over!");
                    reiniciarJuego();
                    return;
                }

                repaint();//Redibujar el panel con la nueva posición
            }
        });

        timer.start();//Inicia el temporizador


    }

    private void actualizarNivel() {
        nivel = puntos / 6 + 1;//Cada 6 puntos sube un nivel
        trampasPorNivel = nivel * 2;//2 trampas por nivel

        //Regenera trampas con la nueva cantidad
        trampas = generarTrampas(trampasPorNivel);
    }

    private void reiniciarJuego() {
        // Resetear puntos y velocidad
        puntos = 0;
        velocidad = 100; // velocidad inicial, igual que cuando empieza el juego
        nivel = 1;
        trampasPorNivel = 2;
        trampas = generarTrampas(trampasPorNivel);
        // Resetear la serpiente
        cuerpoSerpiente.clear();
        cuerpoSerpiente.add(new Point(100, 100));

        // Resetear dirección inicial
        direccionX = 20;
        direccionY = 0;

        // Generar nueva comida
        comida = generarComida();

        // Actualizar el delay del timer y reiniciarlo
        timer.setDelay(velocidad);
        timer.start();


        // Actualizar la pantalla y puntos
        repaint();
    }

    private Point generarComida() {
        Point nuevaComida;
        //Metodo privado para generar comida
        //Con este bucle evitamos que la comida aparezca dentro de la serpiente
        do {
            int x = (int) (Math.random() * (ANCHO / 20)) * 20;
            int y = (int) (Math.random() * (ALTO / 20)) * 20;
            nuevaComida = new Point(x, y);
        } while (cuerpoSerpiente.contains(nuevaComida) || trampas.contains(nuevaComida));
        return nuevaComida;
    }

    private LinkedList<Point> generarTrampas(int cantidad) {
        LinkedList<Point> trampasNuevas = new LinkedList<>();
        for (int i = 0; i < cantidad; i++) {
            Point t;
            do {
                int x = (int) (Math.random() * (ANCHO / 20)) * 20;
                int y = (int) (Math.random() * (ALTO / 20)) * 20;
                t = new Point(x, y);
                //Nos aseguramos que la trampa no este encima de la serpiente
            } while (cuerpoSerpiente.contains(t) || t.equals(comida) || trampasNuevas.contains(t));
            trampasNuevas.add(t);
        }
        return trampasNuevas;
    }


    //Este metodo se llama automaticamente cuando tiene que dibujar algo en la pantalla
    @Override
    protected void paintComponent(Graphics g) {//Dibuja!
        //Esta linea borra lo que habia antes y limpia el panel
        //IMPORTANTE PONERLA
        super.paintComponent(g);

        // Aquí dibujaremos la serpiente, comida, etc.
        // Por ahora vamos a dibujar un rectángulo verde como prueba

        g.setColor(Color.red);
        g.fillRect(comida.x, comida.y, 20, 20);
        g.setColor(Color.GREEN);
        //Dibuja el rectángulo en la pantalla
        for (int i = 0; i < cuerpoSerpiente.size(); i++) {
            Point p = cuerpoSerpiente.get(i);
            g.fillOval(p.x, p.y, 20, 20);//es un circulo
        }
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < trampas.size(); i++) {
            Point t = trampas.get(i);
            g.fillRect(t.x, t.y, 20, 20);
        }
        g.setColor(Color.BLUE);
        g.setFont(new Font("SanSerif", Font.BOLD, 18));
        g.drawString("Puntos: " + puntos, 10, 20);
        //10, 20 ? son las coordenadas (x, y) donde empieza a dibujar el texto
    }


    //Cuando implementamos una interfaz en java te obliga a definir sus metodos (KeyListener) es una interfaz
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
     /*
     public void keyPressed(KeyEvent e)
     Eso significa: "cuando se pulsa una tecla, se crea un objeto
     KeyEvent llamado e que contiene toda la información de esa tecla".
    */
    public void keyPressed(KeyEvent e) {
        // Aquí pondrás el código para cambiar la dirección
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            direccionX = 0;
            direccionY = -20;
        } else if (key == KeyEvent.VK_DOWN) {
            direccionX = 0;
            direccionY = 20;
        } else if (key == KeyEvent.VK_LEFT) {
            direccionX = -20;
            direccionY = 0;
        } else if (key == KeyEvent.VK_RIGHT) {
            direccionX = 20;
            direccionY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Tampoco hace nada por ahora
    }
}

