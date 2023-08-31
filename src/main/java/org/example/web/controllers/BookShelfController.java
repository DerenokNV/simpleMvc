package org.example.web.controllers;


import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookRegexToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping(value = "books")
@Scope("singleton")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books( Model model ) {
      logger.info("BookShelfController.books = " + this.toString() );
      model.addAttribute("book", new Book() );
      model.addAttribute("bookRegexToRemove", new BookRegexToRemove() );
      model.addAttribute("bookList", bookService.getAllBooks() );
      return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook( @Valid Book book, BindingResult bindingResult, Model model ){
      logger.info("BookShelfController.saveBook : " + bindingResult.hasErrors() );
      if ( bindingResult.hasErrors() ){
        model.addAttribute("book", book);
        model.addAttribute("bookRegexToRemove", new BookRegexToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
      } else {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
      }
    }

    @PostMapping("/removeByRegex")
    public String removeByRegexBook( @Valid BookRegexToRemove bookRegexToRemove, BindingResult bindingResult, Model model ){
      logger.info( "removeByRegex: " + bookRegexToRemove );
      if ( bindingResult.hasErrors() ) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
      } else {
        bookService.removeBookByRegex(bookRegexToRemove.getRegexValue());
        return "redirect:/books/shelf";
      }
    }

    @PostMapping("/uploadFile")
    public String uploadFile( @RequestParam("file") MultipartFile file ) throws Exception {
      String strReturn = "redirect:/books/shelf";
      if ( file.getSize() == 0 ) {
        return strReturn;
      }
      String name = file.getOriginalFilename();
      byte[] bytes = file.getBytes();

      String rootPath = System.getProperty( "catalina.home" );
      File dir = new File( rootPath + File.separator + "external_uploads" );
      if ( !dir.exists() ) {
        dir.mkdir();
      }

      File serverFile = new File( dir.getAbsolutePath() + File.separator + name );
      BufferedOutputStream stream = new BufferedOutputStream( new FileOutputStream( serverFile ) );
      stream.write( bytes );
      stream.close();

      logger.info( "new file saved at:" + serverFile.getAbsolutePath() );
      return strReturn;
    }
}
