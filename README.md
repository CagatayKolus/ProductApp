# ProductApp
An android application which uses Product API and Review API for listing products and reviews.

## Prerequisites

#### 1. API Setup

Clone the following repository for docker environment that you can run in your machine.

	git clone https://bitbucket.org/adichallenge/product-reviews-docker-composer.git

Next thing is to run the composer. This is going to run on your computer a few services that you query using REST API. 

	docker-compose up

#### 2. Edit API URL's and Check the Database

- Change PRODUCT_API_URL and REVIEW_API_URL with your current IP Address. 
- Also add your IP address to the network_security_config.xml file.
- Make sure there is no duplicate item (same product id) in the Product API database.

#### 3. Check the Swagger Documentation

- Product API: http://localhost:3001/api-docs/swagger/
- Review API: http://localhost:3002/api/

#### 4. Ready to run.

## Features
- Product Features (Filtering, Listing, Adding, Editing, Deleting)
- Review Features (Listing, Adding)
- Offline Capability
- Pull to Refresh
- Unit Tests

## Tech Stack

- **Kotlin** - Officially supported programming language for Android development by Google
- **Kotlin DSL** - Alternative syntax to the Groovy DSL
- **Coroutines** - Perform asynchronous operations
- **Flow** - Handle the stream of data asynchronously
- **Android Architecture Components**
  - **LiveData** - Notify views about data changes
  - **Room** - Persistence library
  - **ViewModel** - UI related data holder
  - **ViewBinding** - Allows to more easily write code that interacts with views
- **Hilt** - Dependency Injection framework
- **Retrofit** - Networking library
- **Moshi** - A modern JSON library for Kotlin and Java
 
## Screenshots
![productapp](https://user-images.githubusercontent.com/25778714/118157930-cf515100-b423-11eb-967d-f9f6d9739c9d.png)

## Architecture
![arch500](https://user-images.githubusercontent.com/25778714/113482640-3801f100-94a8-11eb-98d6-e15cb21a905b.png)

## Local Unit Tests
- The project uses MockWebServer (scriptable web server) to test Product API and Review API interactions.
