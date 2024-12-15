package com.inviko.proyecto.controller;

import com.inviko.proyecto.model.AsignarAmigoInvisible;
import com.inviko.proyecto.model.Grupo;
import com.inviko.proyecto.model.Usuario;
import com.inviko.proyecto.repository.GrupoRepository;
import com.inviko.proyecto.service.AmigoInvisibleService;
import com.inviko.proyecto.service.GrupoService;
import com.inviko.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GrupoController {

    private final GrupoService grupoService;
    private final AmigoInvisibleService amigoInvisibleService;
    private final UsuarioService usuarioService;
    private GrupoRepository grupoRepository;

    public GrupoController(GrupoService grupoService, AmigoInvisibleService amigoInvisibleService, UsuarioService usuarioService) {
        this.grupoService = grupoService;
        this.amigoInvisibleService = amigoInvisibleService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/grupo")
    public String grupo(Model model){
        List<Grupo> grupoList = grupoService.getAllGrupos();
        model.addAttribute("grupoList", grupoList);
        return "grupo";
    }

    @GetMapping("/crearGrupo")
    public String crearGrupo(Model model){
        Grupo grupo = new Grupo();
        model.addAttribute("grupo", grupo);
        return "crearGrupo";
    }

    @PostMapping("/saveGrupo")
    public String saveGrupo(@Valid @ModelAttribute("grupo") Grupo grupo, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if(bindingResult.hasErrors()){
            return "crearGrupo";
        }
        grupoService.saveGrupo(grupo);
        redirectAttributes.addFlashAttribute("mensaje", "Grupo guardado correctamente");
        return "redirect:/grupo";
    }

    @GetMapping("/grupo/{id}")
    public String mostrarGrupo(@PathVariable Long id, Model model) {
        Grupo grupo = grupoService.findGrupoById(id).orElse(null);
        List<Usuario> usuariosDelGrupo = grupo != null ? grupo.getUsuarios() : List.of(); // Obtener los usuarios del grupo
        List<Usuario> usuarios = usuarioService.getAllUsuarios(); // Obtener todos los usuarios disponibles
        model.addAttribute("grupo", grupo);
        model.addAttribute("usuariosDelGrupo", usuariosDelGrupo); // Pasar los usuarios al modelo
        model.addAttribute("usuarios", usuarios); // Pasar la lista de todos los usuarios al modelo
        return "mostrarGrupo";  // La vista que muestra el grupo y sus usuarios
    }



    @GetMapping("/grupo/{id}/editar")
    public String editarGrupo(@PathVariable Long id, Model model){
        Grupo grupo = grupoService.findGrupoById(id).orElse(null);
        model.addAttribute("grupo", grupo);
        return "crearGrupo";
    }

    @GetMapping("/grupo/{id}/borrar")
    public String borrarGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes){
        grupoService.deleteGrupo(id);
        redirectAttributes.addFlashAttribute("mensaje", "Grupo eliminado correctamente");
        return "redirect:/grupo";
    }

    // Mostrar el formulario para asignar un usuario al grupo
    @GetMapping("/{id}/asignarUsuario")
    public String asignarUsuario(@PathVariable Long id, Model model) {
        // Obtener el grupo y los usuarios
        Grupo grupo = grupoService.findGrupoById(id).orElse(null);
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        // Pasar el grupo y los usuarios al modelo
        model.addAttribute("grupo", grupo);
        model.addAttribute("usuarios", usuarios);

        return "asignar_usuario_a_grupo"; // Nombre de la vista donde seleccionarás el usuario
    }

    @PostMapping("/grupo/{grupoId}/borrarUsuario/{usuarioId}")
    public String borrarUsuarioDelGrupo(@PathVariable Long grupoId, @PathVariable Long usuarioId, RedirectAttributes redirectAttributes) {
        try {
            // Buscar el grupo
            Grupo grupo = grupoService.findGrupoById(grupoId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

            // Buscar el usuario
            Usuario usuario = usuarioService.findUsuarioById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que el usuario pertenece al grupo
            if (!grupo.getUsuarios().contains(usuario)) {
                throw new RuntimeException("El usuario no pertenece a este grupo.");
            }

            // Eliminar la relación ManyToMany
            grupo.eliminarUsuario(usuario);

            // Guardar el grupo actualizado
            grupoService.saveGrupo(grupo);

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente del grupo.");
        } catch (RuntimeException e) {
            // Manejo de errores
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }

        // Redirigir a la página del grupo
        return "redirect:/grupo/" + grupoId;
    }





    // Asignar el usuario seleccionado al grupo
    @PostMapping("/{id}/asignarUsuario")
    public String asignarUsuarioAlGrupo(@PathVariable Long id, @RequestParam("usuarioId") Long usuarioId, RedirectAttributes redirectAttributes) {
        // Obtener el grupo y el usuario desde la base de datos
        Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        Usuario usuario = usuarioService.findUsuarioById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Lógica para asignar el usuario al grupo
        grupo.addUsuario(usuario);  // Esto asegura que tanto el grupo como el usuario estén actualizados

        // Guardar el grupo con el usuario asignado
        grupoService.saveGrupo(grupo);  // También guarda la relación ManyToMany

        redirectAttributes.addFlashAttribute("mensaje", "Usuario asignado al grupo correctamente.");
        return "redirect:/grupo/" + id;  // Redirigir al grupo después de asignar el usuario
    }

    @GetMapping("/{id}/amigoInvisible")
    public String mostrarAmigoInvisible(@PathVariable Long id, Model model) {
        // Obtener el grupo por ID
        Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        // Obtener las asignaciones de los amigos invisibles para ese grupo
        List<AsignarAmigoInvisible> asignaciones = amigoInvisibleService.jugarAmigoInvisible(id);

        // Pasar el grupo y las asignaciones a la vista
        model.addAttribute("grupo", grupo);
        model.addAttribute("asignaciones", asignaciones);

        return "amigoInvisible"; // El nombre de la vista Thymeleaf
    }

    @GetMapping("/{id}/jugar")
    public String jugarAmigoInvisible(@PathVariable Long id, Model model) {
        List<AsignarAmigoInvisible> asignaciones = amigoInvisibleService.jugarAmigoInvisible(id);

        Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        model.addAttribute("grupo", grupo);
        model.addAttribute("asignaciones", asignaciones);

        return "amigoInvisible";
    }

}
