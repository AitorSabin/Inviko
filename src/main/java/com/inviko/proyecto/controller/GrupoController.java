package com.inviko.proyecto.controller;

import com.inviko.proyecto.model.AsignarAmigoInvisible;
import com.inviko.proyecto.model.Grupo;
import com.inviko.proyecto.model.Usuario;
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
    public String saveGrupo(@Valid @ModelAttribute("grupo") Grupo grupo, BindingResult bindingResult, RedirectAttributes redirectAttributes){

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
        List<Usuario> usuariosDelGrupo = grupo != null ? grupo.getUsuarios() : List.of();
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        model.addAttribute("grupo", grupo);
        model.addAttribute("usuariosDelGrupo", usuariosDelGrupo);
        model.addAttribute("usuarios", usuarios);
        return "mostrarGrupo";
    }



    @GetMapping("/grupo/{id}/editar")
    public String editarGrupo(@PathVariable Long id, Model model){

        Grupo grupo = grupoService.findGrupoById(id).orElse(null);
        model.addAttribute("grupo", grupo);
        return "crearGrupo";
    }

    @GetMapping("/grupo/{id}/borrar")
    public String borrarGrupo(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

            if (grupo.getUsuarios() != null && !grupo.getUsuarios().isEmpty()) {
                for (Usuario usuario : grupo.getUsuarios()) {
                    usuario.eliminarGrupo(grupo);
                    usuarioService.saveUsuario(usuario);
                }
            }

            grupoService.deleteGrupo(id);

            redirectAttributes.addFlashAttribute("mensaje", "Grupo eliminado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el grupo: " + e.getMessage());
        }

        return "redirect:/grupo";
    }

    @GetMapping("/{id}/asignarUsuario")
    public String asignarUsuario(@PathVariable Long id, Model model) {

        Grupo grupo = grupoService.findGrupoById(id).orElse(null);
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        model.addAttribute("grupo", grupo);
        model.addAttribute("usuarios", usuarios);

        return "asignar_usuario_a_grupo";
    }

    @PostMapping("/grupo/{grupoId}/borrarUsuario/{usuarioId}")
    public String borrarUsuarioDelGrupo(@PathVariable Long grupoId, @PathVariable Long usuarioId, RedirectAttributes redirectAttributes) {

        try {
            Grupo grupo = grupoService.findGrupoById(grupoId).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

            Usuario usuario = usuarioService.findUsuarioById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!grupo.getUsuarios().contains(usuario)) {
                throw new RuntimeException("El usuario no pertenece a este grupo.");
            }

            grupo.eliminarUsuario(usuario);

            grupoService.saveGrupo(grupo);

            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente del grupo.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }

        return "redirect:/grupo/" + grupoId;
    }

    @PostMapping("/{id}/asignarUsuario")
    public String asignarUsuarioAlGrupo(@PathVariable Long id, @RequestParam("usuarioId") Long usuarioId, RedirectAttributes redirectAttributes) {

        Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        Usuario usuario = usuarioService.findUsuarioById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        grupo.addUsuario(usuario);

        grupoService.saveGrupo(grupo);

        redirectAttributes.addFlashAttribute("mensaje", "Usuario asignado al grupo correctamente.");
        return "redirect:/grupo/" + id;
    }

    @GetMapping("/{id}/amigoInvisible")
    public String mostrarAmigoInvisible(@PathVariable Long id, Model model) {

        Grupo grupo = grupoService.findGrupoById(id).orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        List<AsignarAmigoInvisible> asignaciones = amigoInvisibleService.jugarAmigoInvisible(id);

        model.addAttribute("grupo", grupo);
        model.addAttribute("asignaciones", asignaciones);

        return "amigoInvisible";
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
