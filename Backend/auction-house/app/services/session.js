import Service from '@ember/service';
import Cookies from 'ember-cli-js-cookie';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';

export default Service.extend({
  authToken: Cookies.get('auth-token'),
  userID: Cookies.get('user-id')
});
