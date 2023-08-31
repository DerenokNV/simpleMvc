package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.app.toolset.QueryLoader;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class BookRepository<T> implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private ApplicationContext context;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final Map<String,String> nameColumnBook = new HashMap<>(); { nameColumnBook.put( "id", "deleteBooksId" );
                                                                         nameColumnBook.put( "author", "deleteBooksAuthor" );
                                                                         nameColumnBook.put( "title", "deleteBooksTitle" );
                                                                         nameColumnBook.put( "size", "deleteBooksSize" );
                                                                       };

    @Autowired
    public BookRepository( NamedParameterJdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
      String query = QueryLoader.getInstance().getQuery( "query.xml", "getBooks" );
      if ( query == null ) {
        return new ArrayList<>();
      }

      List<Book> books = jdbcTemplate.query( query, (ResultSet rs, int rowNum) -> {
         Book book = new Book();
         book.setId( rs.getInt( "id" ) );
         book.setAuthor( rs.getString( "author" ) );
         book.setTitle( rs.getString( "title" ) );
         book.setSize( rs.getInt( "size" ) );
         return book;
         });
      return books;
    }

    @Override
    public void store( Book book ) {
      String query = QueryLoader.getInstance().getQuery( "query.xml", "insertBooks" );
      if ( query == null ) {
        return;
      }

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue( "author", book.getAuthor() );
      parameterSource.addValue( "title", book.getTitle() );
      parameterSource.addValue( "size", book.getSize() );
      jdbcTemplate.update( query, parameterSource );

      logger.info( "store new book: " + book );
    }

    public boolean removeItemById( Integer bookIdToRemove ) {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue( "id", bookIdToRemove );

      try {
        jdbcTemplate.update ( QueryLoader.getInstance().getQuery( "query.xml", "deleteBooksId" ), parameterSource );
      } catch (NullPointerException | DataAccessException e) {
        return false;
      }
      logger.info( "remove book completed" );
      return true;
    }

   @Override
   public boolean removeItemByRegex( String regexValue ) {
     logger.info( "remove book removeBookByRegex = " + regexValue );
     String[] params = regexValue.split( ":" );

     return removeItemByStringParam( params[0], params[1] );
   }

    public boolean removeItemByStringParam( String key, String value ) {
      String query = QueryLoader.getInstance().getQuery( "query.xml", nameColumnBook.get( key ) );
      if ( query == null ) {
        return false;
      }
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue( key, value );
      jdbcTemplate.update( query, parameterSource );
      return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void defaultInit() {
      logger.info("BookRepository - default INIT in book repo bean");
    }

    private void defaultDestroy() {
      logger.info("BookRepository - default DESTROY in book repo bean");
    }
}

