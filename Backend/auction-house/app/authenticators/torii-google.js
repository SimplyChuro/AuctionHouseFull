import Torii from 'ember-simple-auth/authenticators/torii';
import ENV from 'auction-house/config/environment';
import RSVP from 'rsvp';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import { inject as service } from '@ember/service';

export default Torii.extend({

  torii: service('torii'),
  customSession: service(),
  
  authenticate(provider, options) {
    var _this = this;
    return this.get('torii').open(provider, options).then((response) => {
      return new RSVP.Promise(function(resolve, reject) {
        return $.ajax({
          url: ENV.HOST_URL+'/api/v1/login/google',
          type: 'POST',
          data: JSON.stringify({  
            code: response.authorizationCode,
            redirectURI: response.redirectUri
          }),
          contentType: 'application/json;charset=utf-8',
          dataType: 'json',
          success: function(data) {
            Cookies.set('auth-token', data.authToken , { expires: 0.1 });
            Cookies.set('user-id', data.userID , { expires: 0.1 });
            Cookies.set('admin-checker', data.adminChecker , { expires: 0.1 });
            _this.set('customSession.authToken', data.authToken);
            _this.set('customSession.userID', data.userID);
            _this.set('customSession.adminChecker', data.adminChecker);
            resolve();
          },
          error: function () {
            reject();
          }
        })
      });
    });
  }

});