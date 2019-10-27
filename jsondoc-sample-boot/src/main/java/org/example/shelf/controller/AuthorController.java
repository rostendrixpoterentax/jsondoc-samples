package org.example.shelf.controller;

import java.util.List;
import java.util.Optional;

import org.example.shelf.documentation.DocumentationConstants;
import org.example.shelf.exception.ItemNotFoundException;
import org.example.shelf.model.Author;
import org.example.shelf.repository.AuthorRepository;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "The author services", name = "Author services", group = DocumentationConstants.GROUP_LIBRARY)
@ApiAuthToken(roles = { "*" }, testtokens = "abc", scheme = "Bearer")
public class AuthorController {

	@Autowired
	private AuthorRepository authorRepository;

	@ApiMethod(id = DocumentationConstants.AUTHOR_FIND_ONE)
	@ApiAuthToken
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ApiResponseObject Author findOne(@ApiPathParam(name = "id") @PathVariable Long id) throws ItemNotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		if(!author.isPresent())
			throw new ItemNotFoundException();
		return author.get();
	}

	@ApiMethod(id = DocumentationConstants.AUTHOR_FIND_ALL)
	@RequestMapping(method = RequestMethod.GET)
	public @ApiResponseObject List<Author> findAll() {
		return authorRepository.findAll();
	}

	@ApiMethod(id = DocumentationConstants.AUTHOR_SAVE)
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ApiResponseObject ResponseEntity<Void> save(@ApiBodyObject @RequestBody Author author, UriComponentsBuilder uriComponentsBuilder) {
		authorRepository.save(author);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponentsBuilder.path("/authors/{id}").buildAndExpand(author.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@ApiMethod(id = DocumentationConstants.AUTHOR_DELETE)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@ApiPathParam(name = "id") @PathVariable Long id) throws ItemNotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		if(!author.isPresent())
			throw new ItemNotFoundException();
		authorRepository.delete(author.get());
	}

}
