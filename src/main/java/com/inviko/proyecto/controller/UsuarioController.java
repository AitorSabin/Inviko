package com.inviko.proyecto.controller;

import com.inviko.proyecto.model.Usuario;
import com.inviko.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UsuarioController {

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
  }

  @GetMapping("/usuario")
  public String usuario(Model model) {
      List<Usuario> usuarioList = usuarioService.getAllUsuarios();
      model.addAttribute("usuarioList", usuarioList);
      return "usuario";
  }

  @GetMapping("/crearUsuario")
  public String crearUsuario(Model model) {
      Usuario usuario = new Usuario();
      model.addAttribute("usuario", usuario);
      return "crearUsuario";
  }

  @PostMapping("/saveUsuario")
  public String saveUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
      if (result.hasErrors()) {
          return "crearUsuario";
      }
      usuarioService.saveUsuario(usuario);
      redirectAttributes.addFlashAttribute("mensaje", "Usuario creado correctamente");
      return "redirect:/usuario";
  }

  @GetMapping("/usuario/{id}")
  public String mostrarUsuario(@PathVariable("id") Long id, Model model) {
      Usuario usuario = usuarioService.findUsuarioById(id).orElse(null);
      model.addAttribute("usuario", usuario);
      return "mostrarUsuario";
  }

  @GetMapping("/usuario/{id}/editar")
  public String editarUsuario(@PathVariable("id") Long id, Model model) {
      Usuario usuario = usuarioService.findUsuarioById(id).orElse(null);
      model.addAttribute("usuario", usuario);
      return "crearUsuario";
  }

  @GetMapping("/usuario/{id}/borrar")
  public String borrarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
      usuarioService.deleteUsuario(id);
      redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
      return "redirect:/usuario";
  }

}
