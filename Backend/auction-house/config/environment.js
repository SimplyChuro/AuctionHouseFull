'use strict';

module.exports = function(environment) {
  let ENV = {
    modulePrefix: 'auction-house',
    environment,
    rootURL: '/',
    locationType: 'auto',
    USER_TOKEN: '',
    HOST_URL: 'https://auction-house-backend.herokuapp.com',
    EmberENV: {
      FEATURES: {},
      EXTEND_PROTOTYPES: {
        Date: false
      }
    },

    APP: {},

    stripe: {
      publishableKey: 'pk_test_594FKlw0ndP41UyAWHNWjBpm'
    },

    torii: {
      allowUnsafeRedirect: true,

      providers: {
        'google-oauth2': {
          apiKey: '182856413608-j9ul6k3brl3tpgsnecqfpuhrb9tba9bs.apps.googleusercontent.com',
          redirectUri: '',
          scope: 'https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile'
        },
          
        'facebook-oauth2': {
          apiKey: '340789379843748',
          redirectUri: '',
          scope: 'public_profile,email'
        }

      }
    }

  };

  if (environment === 'development') {
    ENV.HOST_URL = 'http://localhost:9000';
    ENV.torii.providers['google-oauth2'].redirectUri = 'http://localhost:4200/login/torii/redirect.html';
    ENV.torii.providers['facebook-oauth2'].redirectUri = 'http://localhost:4200/login/torii/redirect.html';
  }

  if (environment === 'production') {
    ENV.HOST_URL = 'https://auction-house-backend.herokuapp.com';
    ENV.torii.providers['google-oauth2'].redirectUri = 'https://auction-house-frontend.herokuapp.com/login/torii/redirect.html';
    ENV.torii.providers['facebook-oauth2'].redirectUri = 'https://auction-house-frontend.herokuapp.com/login/torii/redirect.html';
  }

  return ENV;
};
