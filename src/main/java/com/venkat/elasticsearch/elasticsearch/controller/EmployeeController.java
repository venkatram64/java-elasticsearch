package com.venkat.elasticsearch.elasticsearch.controller;


import com.google.gson.Gson;
import com.venkat.elasticsearch.elasticsearch.config.ElasticsearchClientConfiguration;
import com.venkat.elasticsearch.elasticsearch.model.Employee;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private ElasticsearchClientConfiguration clientConfiguration;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createEmployee(Employee emp) throws UnknownHostException {
        IndexResponse response = clientConfiguration
                .transportClient()
                .prepareIndex("employee","history")
                .setSource(new Gson().toJson(emp)).get();

        return response.getResult().toString();
    }

    @GetMapping("/getById/{id}")
    public Map<String, Object> getEmpById(@PathVariable final String id) throws UnknownHostException{
        GetResponse response = clientConfiguration.transportClient()
                .prepareGet("employee", "history", id).get();
        return response.getSource();
    }

    @GetMapping("/findAll")
    public Map<String, ProfileShardResult> findAll() throws UnknownHostException{
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientConfiguration.transportClient()
                    .prepareSearch("employee")
                    .setTypes("history")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setScroll(new TimeValue(60000))
                    .setQuery(queryBuilder)
                    .setSize(100)
                    .execute()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Iterator<SearchHit> searchHitIterator = searchResponse.getHits().iterator();
        while(searchHitIterator.hasNext()){
            //System.out.println(searchHitIterator.toString());
            SearchHit s = searchHitIterator.next();
            Map<String, Object> source = s.getSource();

            Set<String> stringSet = source.keySet();
            Iterator<String> stringIterator = stringSet.iterator();
            while(stringIterator.hasNext()){
                System.out.println(source.get(stringIterator.next()));
            }

        }

        return searchResponse.getProfileResults();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateEmployee(Employee emp) throws UnknownHostException {
        UpdateRequest updateRequest = new UpdateRequest();

        updateRequest
                .index("employee")
                .type("current")
                .doc(new Gson().toJson(emp));

        UpdateResponse response = null;
        try {
            response = clientConfiguration.transportClient()
                    .update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response.status().toString();

    }



    @RequestMapping(value="/{id}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteById(@PathVariable String id) throws UnknownHostException{
        DeleteResponse response = clientConfiguration.transportClient()
                .prepareDelete("employee","id", id).get();
        return response.getResult().toString();
    }


}
