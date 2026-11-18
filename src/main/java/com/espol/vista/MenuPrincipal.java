package com.espol.vista;

import com.espol.controlador.SistemaManager;

public class MenuPrincipal {
    public void desplegar() {
        SistemaManager sistema = new SistemaManager();
        sistema.inicializarApp();
        Consola consola = new Consola();
        MenuActividades actividades = new MenuActividades(sistema, consola);
        MenuHabitos habitos = new MenuHabitos(sistema, consola);
        VistaMemoria memoria = new VistaMemoria(sistema, consola);

        int opcion;
        do {
            System.out.println("\n=== FOCUSFLOW ===");
            System.out.println("1. Gestion de Actividades");
            System.out.println("2. Tecnicas de Enfoque (Manejo de tiempo)");
            System.out.println("3. Control de hidratacion");
            System.out.println("4. Registro diario de Sostenibilidad");
            System.out.println("5. Juego de memoria");
            System.out.println("6. Salir");
            opcion = consola.entero("Ingrese su opcion: ", 6);

            switch (opcion) {
                case 1:
                    actividades.gestionar();
                    break;
                case 2:
                    actividades.enfoque();
                    break;
                case 3:
                    habitos.hidratacion();
                    break;
                case 4:
                    habitos.sostenibilidad();
                    break;
                case 5:
                    memoria.jugar();
                    break;
                default:
                    break;
            }
        } while (opcion != 0 && opcion != 6);
        System.out.println("Hasta pronto.");
    }
}
