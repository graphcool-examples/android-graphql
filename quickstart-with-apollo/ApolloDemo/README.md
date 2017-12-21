# Android & Apollo Quickstart

* [Apollo Client](https://github.com/apollographql/apollo-client): Fully-featured, production ready caching GraphQL client
* [Graphcool](https://www.graph.cool): Flexible backend platform combining GraphQL + AWS Lambda

<!--

For more information on how to get started refer to the [full android-apollo-instagram tutorial](https://www.graph.cool/docs/quickstart/android-apollo-instagram/).

-->

## Quickstart

### 1. Clone example repository

```sh
git clone https://github.com/graphcool-examples/android-graphql.git
cd android-graphql/quickstart-with-apollo/ApolloDemo
```


### 2. Create Graphcool service with the [Graphcool CLI](https://docs-next.graph.cool/reference/graphcool-cli/overview-zboghez5go)

```sh
# Install Graphcool Framework CLI
npm install -g graphcool

# Create a new service inside a directory called `server`
graphcool init server
```

This created the following file structure in the current directory:

```
.
└── server
    ├── graphcool.yml
    ├── types.graphql
    └── src
        ├── hello.graphql
        └── hello.js
```

### 3. Define data model

Next, you need to define your data model inside the newly created `types.graphql`-file.

Replace the current contents in `types.graphql` with the following type definition (you can delete the predefined `User` type):

```graphql
type Post @model {
  # Required system field
  id: ID! @isUnique # read-only (managed by Graphcool)

  # Optional system fields (remove if not needed)
  createdAt: DateTime! # read-only (managed by Graphcool)
  updatedAt: DateTime! # read-only (managed by Graphcool)

  description: String!
  imageUrl: String!
}
```

### 4. Deploy the GraphQL server

You're now ready to put your Graphcool service into production! Navigate into the `server` directory and [deploy](https://docs-next.graph.cool/reference/graphcool-cli/commands-aiteerae6l#graphcool-deploy) the service:

```sh
cd server
graphcool deploy
```

When prompted which cluster you want to deploy to, choose any of the **Backend-as-a-Service** options (`shared-eu-west-1`, `shared-ap-northeast-1` or `shared-us-west-2`).

Save the HTTP endpoint for the `Simple API` from the output, you'll need it in the next step.

> **Note**: You can now test your GraphQL API inside a GraphQL playground. Simply type the `graphcool playground` command and start sending queries and mutations.


### 5. Connect the app with your GraphQL API

Paste the `Simple API` endpoint into `ApolloDemoApplication.java` as the value for the `final` variable `BASE_URL` that's used to instantiate the `ApolloClient`:

```js
// replace `__SIMPLE_API_ENDPOINT__` with the endpoint from the previous step
private static final String BASE_URL = "__SIMPLE_API_ENDPOINT__";
```

### 6. Install `apollo-codegen`

To use the Apollo Android Client, you need to install [`apollo-codegen`](https://github.com/apollographql/apollo-codegen), a command line tool that will generate Java classes from your GraphQL queries & mutations at build-time. 

```sh
npm install -g apollo-codegen
```

You can find more info the installation process in the [Apollo docs](http://dev.apollodata.com/android/installation.html).

### 7. Run locally

Start the app in Android Studio 🚀

## Next steps

* [Documentation](https://docs-next.graph.cool)
* [Advanced GraphQL features](https://www.graph.cool/docs/tutorials/advanced-features-eath7duf7d/)
* [Authentication & Permissions](https://www.graph.cool/docs/reference/authorization/overview-iegoo0heez/)
* [Implementing business logic with serverless functions](https://www.graph.cool/docs/reference/functions/overview-boo6uteemo/)


## Help & Community [![Slack Status](https://slack.graph.cool/badge.svg)](https://slack.graph.cool)

Join our [Slack community](http://slack.graph.cool/) if you run into issues or have questions. We love talking to you!

![](http://i.imgur.com/5RHR6Ku.png)
