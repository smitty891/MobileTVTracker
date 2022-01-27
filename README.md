# IT3048C - MobileTVTracker
## Introduction  

TvTracker allows users to build a list of movies/series they want to watch. They can search for movies by titles and save it to a view list. 

Uers choose in which streaming platformes such as Netflix, Hulu, IMDB, Amazon Prime Videos, Youtube, etc to wwatch movies/series.

Additionally, TvTarcker will enable users to:

- Have record of movies/series watched by saving to view list

- Mark those watched as "already watched"

- Update the view list by adding or removing movies/series.

- Be able to add comments 

- Upload images.




## Storyboard
### Home/Browse page
![homebrowse](URL)
### Favorites
![Favorites](URL)

# Requirements  

  1_As a user, I want to be able to build a list of movies/series I want to watch.

## Example

Given: A feed of movies data are available

When: The user searches for a movie Cast Away

When: The user selects movie Cast Away

Then: The user’s movie will be saved in a view list.

## Example

Given: Movies data are available

When: The user/service searches for “gggrrrr,-wwgshdjjll”

Then: TvTracker will not return any results, and the user will not be able to save the 

movie.

  2_As a user, I want to be able to see in which streaming platform my movie is 

available.

## Example

Given: The user is logged in and has selected a previously-saved Cast Away movie

When: The user enters the streaming platform to watch the movie/serie

Then: The user can watch the movie. 

  3_As a user, I want to have a record of movies/series I watched.

## Example

Given: The user has a valid account and movies associated to that account.

When: The user marks the movies he/she watched by checking the box "already 

watched"

Then: The user will see the movies he/she already watched.

## Example

Given: The user has a valid account and movies associated to that account.

When: The user wants to delete Cast Away movie from the list

Then: By selecting Cast Away movie from the list and clicking the button DELETE, the user is able to remove this movie from the list.

4_As a user, I want to comment on movies I like it most.  

## Example

Given: The user watches a movie, he/she likes that movie 

When: The user is able to write a comment

Then: The user's feedback will be saved.

## Class Diagram

![TvTrackerDiagram](https://github.com/smitty891/MobileTVTracker/blob/main/TvTrackerUML.png?raw=true)

### Class Diagram Description

**IMediaEntryService** - interface declaring all necessary methods for MediaEntry related functionality.

**MediaEntryService** - contains implementation for all the methods in IMediaEntryService.

**MediaEntryServiceStub** - implements IMediaEntryService's methods with hardcoded return values for initial ui development.

**MediaEntry** -  carries MediaEntry data between processes.

**IMediaEntryDAO** - interface declaring the methods needed for MediaEntry's data access object.

**MediaEntryDAO** - implements IMediaEntryDAO allowing access to MediaEntry records in our underlying database.


## JSON Schema

This is what we plan to export to another app.

>{
>  "type" : "object",
>  "properties" : {
>    "description" : {
>      "type" : "string"
>    },
>    "type" : {
>      "type" : "string"
>    },
>    "itemId" : {
>      "type" : "integer"
>    },
>    "platform" : {
>      "type" : "string"
>    },
>    "title" : {
>      "type" : "string"
>    },
>    "imageUrl" : {
>      "type" : "string"
>    },
>    "watched" : {
>      "type" : "boolean"
>    }
>  }
>}

## Team Members and Roles

UI Developer: Robby Hoover

Integration Developer: Tate Weber, Blake Schriewer, Vedant Amin

Product Owner/GitHub/Scrum:
Ryan Smith

## Milestones

[Milestones](https://github.com/smitty891/MobileTVTracker/projects/1)

## Weekly Stand-up

[Sundays @ 8:00PM](https://teams.microsoft.com/l/meetup-join/19%3ameeting_MGNlN2MzZTYtODZkMy00NmZkLWI0ZjMtNDQ4YjNkMmQ5NDVh%40thread.v2/0?context=%7b%22Tid%22%3a%22f5222e6c-5fc6-48eb-8f03-73db18203b63%22%2c%22Oid%22%3a%22cde19e27-29a9-4f05-b2cb-65028bb3508e%22%7d)
