package com.clinicpet.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Venta;

@Repository
public interface IVentaRepository {

	List<Venta> findByUsuario(Usuario usuario);

	List<Venta> findByUsuarioId(Integer usuarioId);

	List<Venta> findByFechaBetween(Date fechaInicio, Date fechaFin);

	List<Venta> findByFechaAfter(Date fecha);

	List<Venta> findByFechaBefore(Date fecha);

	List<Venta> findByTotalGreaterThan(Double total);

	List<Venta> findByTotalLessThan(Double total);

	List<Venta> findByTotalBetwwen(Double minTotal, Double maxTotal);

	long countByUsuario(Usuario usuario);

	@Query("SELECT SUM(v.total) FROM Venta v WHERE v.usuario.id = :usuarioId")
	Double sumTotalByUsuarioId(@Param("usuarioId") Integer usuarioId);

	@Query("SELECT SUM (v.total) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
	Double sumTotalByFechaBetween(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

	@Query("SELECT v FROM Venta v JOING FETCH v.detallesventa WHERE v.id = :ventaId")
	Venta findVentaWithDetalles(@Param("ventaId") Integer ventaId);

	@Query("SELECT v FROM Venta v LEFT JOIN FETCH v.pago WHERE v.id = :ventaId")
	Venta findVentaWithPago(@Param("ventaId") Integer ventaId);

	@Query("SELECT v FROM Venta v WHERE YEAR(v.fecha) = :year AND MONTH(v.fecha) = :month")
	List<Venta> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

	List<Venta> findTop10ByOrderByFechaDesc();

	List<Venta> findBySubtotalGreaterThan(Double subtotal);

	boolean existByUsuarioAndFecha(Usuario usuario, Date fecha);

	@Query("SELECT DATE(v.fecha), SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :start AND :end GROUP BY DATE(v.fecha)")
	List<Object[]> getDAilySales(@Param("start") Date start, @Param("end") Date end);

	@Query("SELECT v FROM VENTA v LEFT JOIN FETCH v.detallesVenta LEFT JOIN FETCH v.pagp WHERE v.id = :ventaID")
	Venta findVentaCompleta(@Param("ventaId") Integer ventaId);

}
