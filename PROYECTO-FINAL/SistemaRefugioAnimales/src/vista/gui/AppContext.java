package vista.gui;

import controlador.*;

public class AppContext {
    private static AppContext instance;
    public final SeguridadController seguridad;
    public final MascotaController mascotas;
    public final AdoptanteController adoptantes;
    public final AdopcionController adopciones;
    public final HistorialMedicoController historial;

    private AppContext() {
        seguridad = new SeguridadController();
        mascotas = new MascotaController();
        adoptantes = new AdoptanteController();
        adopciones = new AdopcionController(mascotas);
        historial = new HistorialMedicoController();
    }

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }
}