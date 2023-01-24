# Raken_SendGridWrapper
- Requires SENDGRID_API_KEY to be set as an environment variable prior to running, unless preforming a dry run.
- Set environment variable ALLOW_NON_RAKEN_DOMAINS to true to allow sending to non-raken domains. Otherwise, they will be 
stripped from CC and BCC. Non-raken domains are logged under /logs.
- enrich = true in query param adds a quote into the body html. 
- dryrun = true in query param does not send emails.

- Demo curl
```shell
curl --location --request POST 'http://localhost:8080/sendEmail?enrich=true&dryrun=true' \
--header 'Content-Type: application/json' \
--data-raw '{
    "To": "yifanli722@rakenapp.com",
    "CC": ["test@gmail.com", "test2@gmail.com"],
    "BCC": ["test3@gmail.com"],
    "Subject": "Lorem Ipsum",
    "Body": "<!DOCTYPE html> <html> <head> <title>Lorem Ipsum</title> </head> <body> <h1>Lorem Ipsum</h1> <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, leo eget bibendum congue, nibh ipsum gravida velit, vel blandit nibh nibh non orci.</p> <p>Sed auctor, velit id pellentesque tempus, magna libero convallis velit, vel euismod augue velit vel velit.</p> </body> </html>"
}'
```
 
