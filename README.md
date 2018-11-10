Object traversal to highlight values

# Example

Having an object with the following JSON structure...
```json
{
  "infos": {
    "infosValue": "Some additional infos",
    "moreInfos": {
      "subInfosValue": "sub infos on DOE"
    },
    "parentInfos": "parent infos about Doe"
  },
  "addresses": [
    {
      "street": "Nunuapa",
      "city": "San Salvador"
    },
    {
      "street": "Doe street",
      "city": "New Taipei"
    }
  ],
  "emails": [
    "john.doe@gmail.com",
    "john.doe@yahoo.com"
  ],
  "lastname": "DoeDoe",
  "firstname": "John"
}
```
...when you call...

```java
List<Highlight> highlights = new Lighter.Builder(customer, "doe")
                .htmlTag("em")
                .build()
                .highlight();
```

...you will get...
```
[
  {
    "hl": "doe",
    "fullPathFieldName": "addresses[1].street",
    "pathFieldName": "street",
    "originalValue": "Doe street",
    "value": "<em>Doe</em> street"
  },
  {
    "hl": "doe",
    "fullPathFieldName": "emails[0]",
    "pathFieldName": "emails[0]",
    "originalValue": "john.doe@gmail.com",
    "value": "john.<em>doe</em>@gmail.com"
  },
  {
    "hl": "doe",
    "fullPathFieldName": "emails[1]",
    "pathFieldName": "emails[1]",
    "originalValue": "john.doe@yahoo.com",
    "value": "john.<em>doe</em>@yahoo.com"
  },
  {
    "hl": "doe",
    "fullPathFieldName": "infos.moreInfos.subInfosValue",
    "pathFieldName": "subInfosValue",
    "originalValue": "sub infos on DOE",
    "value": "sub infos on <em>DOE</em>"
  },
  {
    "hl": "doe",
    "fullPathFieldName": "infos.parentInfos",
    "pathFieldName": "parentInfos",
    "originalValue": "parent infos about Doe",
    "value": "parent infos about <em>Doe</em>"
  },
  {
    "hl": "doe",
    "fullPathFieldName": "lastname",
    "pathFieldName": "lastname",
    "originalValue": "DoeDoe",
    "value": "<em>Doe</em><em>Doe</em>"
  }
]
```