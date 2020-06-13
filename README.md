# 9w9w

Weather information sample app.  
Shows the weather for Utrecht on first
startup. On consecutive launches, the last user search is restored.

## Setup

- Create an account at [rapidapi.com](https://www.rapidapi.com) and
- subscribe to the following api:
  [Open Weather](https://rapidapi.com/community/api/open-weather-map/)
- Copy your `X-RapidAPI-Key` there.
- Create a file called `secrets.properties` in the root of the project
  with the following contents:
  ```
  rapidapi_key="<X-RapidAPI-Key>"
  ```
