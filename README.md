# SpringBoot Quick Guide
Spring boot CRUD, demos REST API setup and DB access through JPA.
<br>
<br>
Run application using docker compose. 
```shell
docker compose up
```

### API doc
| Endpoint                         | Method | Path Parameters | Request Body   | Response Body                |
|----------------------------------|--------|-----------------|----------------|------------------------------|
| `/api/RetrieveImage/{imageHash}` | GET    | `imageHash`     | -              | Image in Jpeg format         |
| `/api/UploadImage`               | POST   | -               | As binary data | Map: `{ "sha256": String }`  |
| `/api/DeleteImage/{sha256}`      | DELETE | `sha256`        | -              | Map: `{ "deleted": String }` |
 
