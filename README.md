# backend-coin-lift

# Obtaining API Keys from CoinMarketCap and Coindesk

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
3. Save the file and restart your Spring Boot application.

## Obtaining a Coindesk API Key

1. Go to the [RAPID API](https://rapidapi.com/hub) page and sign up for an account, or log in if you already have one.
2. Once you are logged in, navigate to the [Cryptocurrency News API](https://rapidapi.com/topapi-topapi-default/api/cryptocurrency-news2/) page by searching on website or by clicking this [LINK](https://rapidapi.com/topapi-topapi-default/api/cryptocurrency-news2/).
3. Choose a plan that suits your needs and click the "Subscribe" button to proceed to the next step _(For this tutorial, we will be using a free plan)_.
4. Fill in the required information and click the "Submit" button to complete the subscription process. Note that some plans require payment, while others are free.
5. Once you have subscribed to a plan, you will receive an API key, which you can use to access the Coindesk API.

### Adding the API Key to a Spring Boot Project

1. Open the `application.yaml` file of this Spring Boot project, located in the `src/main/resources` directory.
2. Add the following lines to the file, replacing `<YOUR_API_KEY>` with the API key you obtained from Coindesk:

```yaml
rapid:
  coindesk:
    api:
      key: <YOUR_API_KEY>
```
3. Save the file and restart your Spring Boot application.

