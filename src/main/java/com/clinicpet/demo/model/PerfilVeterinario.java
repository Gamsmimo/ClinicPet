package com.clinicpet.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
<<<<<<< HEAD
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
=======
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfilveterinario")
public class PerfilVeterinario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String especialidad;
	private String tarjetaProfesional;
<<<<<<< HEAD
	private Boolean estado; // false: inactivo por defecto hasta que el admin lo apruebe
	private String experiencia;

	// Relación con Usuario
	@OneToOne
	@JoinColumn(name = "usuario_id")
=======
	private String estado;

	// Relación con Usuario
	@OneToOne
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
	private Usuario usuario;

	// Relación con citas
	@OneToMany(mappedBy = "veterinario")
<<<<<<< HEAD
	private List<Cita> citas;

	// relacion con veterinaria
	@ManyToOne
	@JoinColumn(name = "veterinaria_id")
	private Veterinaria veterinaria;

	// constructor vacio
	public PerfilVeterinario() {
		this.estado = false; // inactivo hasta aprobacion

	}

	// constructor con campos
	public PerfilVeterinario(Integer id, String especialidad, String tarjetaProfesional, Boolean estado,
			String experiencia, Usuario usuario, List<Cita> citas, Veterinaria veterinaria) {
=======
	private List<Cita> cita;

	// Relación con emergencia
	@OneToMany(mappedBy = "veterinario")
	private List<Emergencia> emergencia;

	// constructor vacio
	public PerfilVeterinario() {
	}

	// constructor con campos
	public PerfilVeterinario(Integer id, String especialidad, String tarjetaProfesional, String telefono, String estado,
			Usuario usuario, List<Cita> cita, List<Emergencia> emergencia) {
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
		super();
		this.id = id;
		this.especialidad = especialidad;
		this.tarjetaProfesional = tarjetaProfesional;
		this.estado = estado;
<<<<<<< HEAD
		this.experiencia = experiencia;
		this.usuario = usuario;
		this.citas = citas;
		this.veterinaria = veterinaria;
	}

	// getters y setters
=======
		this.usuario = usuario;
		this.cita = cita;
		this.emergencia = emergencia;
	}

>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getTarjetaProfesional() {
		return tarjetaProfesional;
	}

	public void setTarjetaProfesional(String tarjetaProfesional) {
		this.tarjetaProfesional = tarjetaProfesional;
	}

<<<<<<< HEAD
	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}

=======
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

<<<<<<< HEAD
	public List<Cita> getCitas() {
		return citas;
	}

	public void setCitas(List<Cita> citas) {
		this.citas = citas;
	}

	public Veterinaria getVeterinaria() {
		return veterinaria;
	}

	public void setVeterinaria(Veterinaria veterinaria) {
		this.veterinaria = veterinaria;
	}

	// to string
	@Override
	public String toString() {
		return "PerfilVeterinario [id=" + id + ", especialidad=" + especialidad + ", tarjetaProfesional="
				+ tarjetaProfesional + ", estado=" + estado + ", experiencia=" + experiencia + "]";
	}

}
=======
	public List<Cita> getCita() {
		return cita;
	}

	public void setCita(List<Cita> cita) {
		this.cita = cita;
	}

	public List<Emergencia> getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(List<Emergencia> emergencia) {
		this.emergencia = emergencia;
	}

	@Override
	public String toString() {
		return "PerfilVeterinario [id=" + id + ", especialidad=" + especialidad + ", tarjetaProfesional="
				+ tarjetaProfesional + ", estado=" + estado + "]";
	}

}
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
