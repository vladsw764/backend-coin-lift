# Backend for CoinLift.
This project is called CoinLift and involves the development of a backend system for a cryptocurrency application. The team consists of one backend developer, three frontend developer, and three designers.
## Documentation:

#### Swagger Docs -> [LINK](https://backend-coin-lift-production.up.railway.app/swagger-ui/index.html)

## Getting Started

1. Clone the repository to your local machine.
2. Ensure that you have Docker and Docker Compose installed on your system. And run this command in you terminal:
```shell
docker-compose up -d
```
3. Add JWT secret key in the application.yml file:
```yaml
jwt:
  secret: <YOUR_OWN_SECRET_KEY>
```
4. Set up AWS S3 buckets by creating a ~/aws directory in your terminal (**[INSTRUCTION](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-temporary.html)**) and configuring the following settings in the application.yml file:
```yaml
aws:
  region: <AWS_REGION>
  s3:
    buckets:
      customer: <BUCKET_NAME>
```
5. Obtain the API keys from CoinMarketCap and Coindesk **_(instructions below)_**.
6. Add the API keys to the Spring Boot project by updating the application.yaml file:
```yaml
coinmarketcap:
  api:
    key: <YOUR_API_KEY>

rapid:
  coindesk:
    api:
      key: <YOUR_API_KEY>
```
7. Save the application.yaml file and restart the Spring Boot application.
8. Build the project by running `mvn clean package`.
9. Run the project by running `java -jar target/backend-1.jar`.


## Instruction for obtaining API Keys from CoinMarketCap and Coindesk

## Obtaining a CoinmarketCap API Key
1. Go to the [CoinMarketCap](https://coinmarketcap.com) website and sign up for an account, or log in if you already have one.
2. Once you are logged in, navigate to the [API page](https://coinmarketcap.com/api/) by clicking on the "API" link in the top menu.
3. Choose a plan that suits your needs and click the "Subscribe" button to proceed to the next step _(For this tutorial, we will be using a free plan)_.
4. Enter your billing information and click the "Continue" button to complete the subscription process. Note that some plans require payment, while others are free.
5. Once you have subscribed to a plan, you will receive an API key, which you can use to access the CoinMarketCap API.

### Adding the API Key to a Spring Boot Project

1. Open the application.yaml file of this Spring Boot project, located in the src/main/resources directory.
2. Add the following line to the file, replacing <YOUR_API_KEY> with the API key you obtained from CoinMarketCap:
```agsl
coinmarketcap:
    api:
       key: <YOUR_API_KEY>
```

## Obtaining a Coindesk API Key

1. Go to the [RAPID API](https://rapidapi.com/hub) page and sign up for an account, or log in if you already have one.
2. Once you are logged in, navigate to the [Cryptocurrency News API](https://rapidapi.com/topapi-topapi-default/api/cryptocurrency-news2/) page by searching on website or by clicking this [LINK](https://rapidapi.com/topapi-topapi-default/api/cryptocurrency-news2/).
3. Choose a plan that suits your needs and click the "Subscribe" button to proceed to the next step _(For this tutorial, we will be using a free plan)_.
4. Fill in the required information and click the "Submit" button to complete the subscription process. Note that some plans require payment, while others are free.
5. Once you have subscribed to a plan, you will receive an API key, which you can use to access the Coindesk API.

### Adding the API Key to a Spring Boot Project

1. Open the `application.yml` file of this Spring Boot project, located in the `src/main/resources` directory.
2. Add the following lines to the file, replacing `<YOUR_API_KEY>` with the API key you obtained from Coindesk:

```yaml
rapid:
  coindesk:
    api:
      key: <YOUR_API_KEY>
```

 **Please make sure to replace** `<OWN_SECRET_KEY>`, `<AWS_REGION>`, `<BUCKET_NAME>`, `<YOUR_COINMARKETCAP_API_KEY>`, **and** `<YOUR_COINDESK_API_KEY>` **with the actual values you want to use.**

