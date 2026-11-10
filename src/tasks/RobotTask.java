package tasks;

import model.CentralSoldadura;
import model.CintaTransportadora;
import model.SupervisorCentral;

public class RobotTask implements Runnable {

    private final int idRobot;
    private final CintaTransportadora cinta;
    private final CentralSoldadura central;
    private final SupervisorCentral supervisor;
    private int bateria;

    public RobotTask(int idRobot, CintaTransportadora cinta, CentralSoldadura central, SupervisorCentral supervisor) {
        this.idRobot = idRobot;
        this.cinta = cinta;
        this.central = central;
        this.supervisor = supervisor;
        this.bateria = 100;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                supervisor.comprovarAlerta();

                cinta.agafarBateria();
                Thread.sleep(500);

                supervisor.comprovarAlerta();

                int estacio = central.sollicitarEstacio();
                try {
                    Thread.sleep(1000);
                    bateria -= 15;
                    if (bateria < 0) {
                        bateria = 0;
                    }
                } finally {
                    central.alliberarEstacio(estacio);
                }

                supervisor.comprovarAlerta();

                central.signarCertificat();
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getIdRobot() {
        return idRobot;
    }

    public int getBateria() {
        return bateria;
    }

}
