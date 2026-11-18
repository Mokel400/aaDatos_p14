package es.fplumara.dam.ad.p14.retos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import es.fplumara.dam.ad.p14.servicio.Taquilla;
import es.fplumara.dam.ad.p14.util.Conexion;


public class Azul {

    private Connection conexion;


    public static void main(String[] args){

        try (Connection conexion = Conexion.abrir()){
            Taquilla taquilla = new Taquilla(conexion);



        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void comprarConGastos(int idCliente, int idConcierto, int cantidad, boolean simularFallo, Taquilla taquilla) throws SQLException, ClassNotFoundException {
        conexion.setAutoCommit(false);
        try {
            BigDecimal importe = taquilla.obtenerPrecio(idCliente);
            importe = importe.add(BigDecimal.valueOf(1.50));
            taquilla.restarSaldo(idCliente, importe);
            if (simularFallo) {
                throw new SQLException("Fallo simulado: Pago cancelado");
            }
            taquilla.restarEntradas(idConcierto, cantidad);
            taquilla.registrarVenta(idCliente, idConcierto, cantidad, importe);
            conexion.commit();
            System.out.println("Compra confirmada: " + cantidad + "  entradas por " + importe + " €");
        } catch (SQLException e) {
            conexion.rollback();
            System.out.println("Compra cancelada, se deshacen los cambios" + e.getMessage());
        } finally {
            conexion.setAutoCommit(true);
        }
    }
}
