package dtos;

import com.google.gson.annotations.Expose;
/**
 * Clase DTO que representa los datos de un usuario, incluyendo su información personal, correo electrónico, 
 * contraseña y foto. Se utiliza para intercambiar información de usuarios en las operaciones de inicio de sesión y registro.
 * 
 * @author GPR
 * @date 06/03/2025
 */

public class LoginDto {
	 
	@Expose
	private Long idUsuario;
	
	@Expose
	private String nombreCompleto;
	
	@Expose
	private String movil;
	
	@Expose
	private String correo;
	
	@Expose
	private String tipoUsuario;
	
	@Expose
	private String password;
	
	private byte[] foto; 
	
	
	
	public LoginDto(Long idUsuario, String nombreCompleto, String movil, String correo, String tipoUsuario,
			String password, byte[] foto) {
		this.idUsuario = idUsuario;
		this.nombreCompleto = nombreCompleto;
		this.movil = movil;
		this.correo = correo;
		this.tipoUsuario = tipoUsuario;
		this.password = password;
		this.foto = foto;
	}


	public LoginDto() {
	
	}


	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public byte[] getFoto() {
		return foto;
	}


	public void setFoto(byte[] foto) {
		this.foto = foto;
	}	
}
