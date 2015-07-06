# Kitten Savior

This doc describes the few web services exposed by the addon. These services are used by the mobile apps to interact with the addon.

### Root path

`/kittenSavior`

### Meetings

`GET /meetings`

**Description:**

Retrieve all the meetings in the database. By passing the parameter `user`, you can limit the response to only meetings that you created, or those in which you are participating. Meetings are returned in order of creation date, most recent first.

**Parameters:**

Param | Description | Ex value
----- | ----------- | ----
user  | The creator or a participant of a meeting | phil 

**Response:**
```
{
    meetings : [
        {
            id : "1",
            name : "Kitten Savior",
            description : "Let's meet to decide how to save kittens!",
            creator : "username",
            status : "opened", // closed
            participants : [ "phil" , "thib" , "..." ],
            options : [
                {
                    id : "",
                    start_timestamp : 141516,
                    end_timestamp : 141517
                }
            ]
        },
        { ... }
    ]
}
```

### Choices

`GET /meetings/{id}/choices/`

**Description:**

Retrieves the choices of the participants of the given meeting.

**Parameters:**

Param | Description | Value
----- | ----------- | -----
id    | The id of the meeting | Number

**Response:**
```
{
    choices : [
        {
            time_id : "123",
            user : "phil",
            choice : "true" // false
        },
        { ... }
    ]
}
```

`POST /meetings/{id}/choices/`

**Description:**

Sets the choice of the user for the given meeting.

**Parameters:**

Param | Description | Value
----- | ----------- | -----
id    | The id of the meeting | Number

**Request body:**
```
{
    time_id : "123",
    user : "phil",
    choice : "true" // false
}
```

### Users

`GET /users/{username}`

**Description:**

Retrieves the details of the given user.

**Parameters:**

Param | Description | Ex value
----- | ----------- | -----
username | The username of the user | phil

**Response:**

```
{
    username : "phil",
    timezone : "GMT+7",
    first_name : "Philippe",
    last_name : "Aristote"
}
```

### Login

`POST /rest/private/platform/info`

**Description:**

Existing Platform service used to login.

**Headers:**

Header | Description | Ex value
----- | ----------- | -----
Authorization | The authorization header | Basic base64(username:password)

**Responses:**

* 200 : login successful
* 401 : login failed

