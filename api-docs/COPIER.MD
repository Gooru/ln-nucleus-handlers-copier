Features
===========

 - Copy Resource.
 - Copy Question.
 - Copy Collection.
 - Copy Assessment.
 - Copy Course.
 - Copy Unit.
 - Copy Lesson.


###Copy Resource

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus-copier/{version}/resources/{id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |


**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |


**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "target_collection_id": "f6e0f3cc-1915-4f4e-878f-b6d785648ebc"
  }
```

**Location Header**

```
    Location: d84435bf-2d00-4061-928a-5b1c10301404
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_collection_id": "f6e0f3cc-1915-4f4e-878f-b6d785648ebc"
}' http://nucleus.gooru.org/api/nucleus/v1/copier/resources/37c6748a-64ce-48a8-9e10-4dc455e90ea0
```

###Copy Question

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/questions/{id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |


**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |

**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "target_collection_id": "f6e0f3cc-1915-4f4e-878f-b6d785648ebc"
  }
```

**Location Header**

```
    Location: c2d0d9f7-cfa9-437e-a3e7-3d75d60c73ae
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_collection_id": "f6e0f3cc-1915-4f4e-878f-b6d785648ebc"
}' http://nucleus.gooru.org/api/nucleus/v1/copier/questions/56737bfe-f289-4b3a-af9b-ddb3fd0564b7
```

###Copy Collection

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/collections/{collection-id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |

**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |
| target_unit_id | target unit id |
| target_lesson_id | target lesson id |

**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "target_course_id": "0d02a26d-ceb8-48d9-b5e0-779b65cf635f",
    "target_unit_id": "b9040425-ee79-4b37-a970-80f1587fda2a",
    "target_lesson_id": "b9d66d23-0b4c-4910-9664-05fd54fa8da9"
  }
```

**Location Header**

```
    Location: b45dd1de-5b4e-4360-b9c1-becbea3a03a5
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_course_id": "0d02a26d-ceb8-48d9-b5e0-779b65cf635f",
    "target_unit_id": "b9040425-ee79-4b37-a970-80f1587fda2a",
    "target_lesson_id": "b9d66d23-0b4c-4910-9664-05fd54fa8da9"
  }' http://nucleus.gooru.org/api/nucleus/v1/copier/collections/f6e0f3cc-1915-4f4e-878f-b6d785648ebc
```

###Copy Assessment

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/assessments/{assessment-id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |

**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |
| target_unit_id | target unit id |
| target_lesson_id | target lesson id |


**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```

**Response body**

```json
  {
    "target_course_id": "0d02a26d-ceb8-48d9-b5e0-779b65cf635f",
    "target_unit_id": "b9040425-ee79-4b37-a970-80f1587fda2a",
    "target_lesson_id": "b9d66d23-0b4c-4910-9664-05fd54fa8da9"
  }
```

**Location Header**

```
    Location: 936a7b86-a223-4a22-97b2-66efdf9a2592
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_course_id": "0d02a26d-ceb8-48d9-b5e0-779b65cf635f",
    "target_unit_id": "b9040425-ee79-4b37-a970-80f1587fda2a",
    "target_lesson_id": "b9d66d23-0b4c-4910-9664-05fd54fa8da9"
  }' http://nucleus.gooru.org/api/nucleus/v1/copier/assessments/d0e72584-252a-4b6a-b60c-f556dffb37f4
```

###Copy Course

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/courses/{id} |
| Auth | Required |
| Request Body Type | None |
| Request Body  | None |
| Response Code | 201 Successful |


**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```


**Location Header**

```
    Location: 14ec8c64-098b-4dca-b283-83e4ef23c340
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{}' http://nucleus.gooru.org/api/nucleus/v1/copier/courses/af660a5a-95d5-4197-8fa2-19f975d21e0b
```

###Copy Unit

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/courses/{course-id}/units/{unit-id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |

**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |

**Response body**

```json
  {
    "target_course_id": "76f11124-f0c0-4a56-a253-6bbe85ae1084"
  }
```

**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```


**Location Header**

```
    Location: b7770a92-f204-4b9e-b9b8-5a5e66da1543
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_course_id": "76f11124-f0c0-4a56-a253-6bbe85ae1084"
  }' http://nucleus.gooru.org/api/nucleus/v1/copier/courses/af660a5a-95d5-4197-8fa2-19f975d21e0b/units/baa1e6e3-2815-4892-940a-11701921737b
```

###Copy Lesson

**Rest API details**

| Name | Summary |
|------------------------|--------|
| HTTP Method | POST |
| End Point | /api/nucleus/{version}/courses/{course-id}/units/{unit-id}/lessons/{lesson-id} |
| Auth | Required |
| Request Body Type | JSON |
| Response Code | 201 Successful |

**Request Body attributes**

| Name | Comments |
|------|---------|
| target_course_id | target course id |
| target_unit_id | target unit id |

**Response body**

```json
    {
        "target_course_id": "76f11124-f0c0-4a56-a253-6bbe85ae1084",
        "target_unit_id": "0f165f21-861e-4be9-926d-ca997773ab00"
    }
```

**Authorization Header**

```
Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2
```


**Location Header**

```
    Location: 6aa70e90-715f-4d98-9898-35d594657ea8
```

**CURL snippet**

```posh
curl -i  -H "Content-Type: application/json" -H "Authorization: Token MTQ1NzM1ODIwMDI1MTo0Y2M4MWQ4ZS03MjFjLTQ5ZDItYmVmMy00M2EzNjhiNTg4NzA6YmE5NTZhOTctYWUxNS0xMWU1LWEzMDItZjhhOTYzMDY1OTc2" -X POST -d '{
    "target_course_id": "76f11124-f0c0-4a56-a253-6bbe85ae1084",
    "target_unit_id": "0f165f21-861e-4be9-926d-ca997773ab00"
}' http://nucleus.gooru.org/api/nucleus/v1/copier/courses/af660a5a-95d5-4197-8fa2-19f975d21e0b/units/baa1e6e3-2815-4892-940a-11701921737b/lessons/b9836ee7-01c2-47be-83cf-ed7ff4c95e47
```
