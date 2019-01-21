import Service from '@ember/service';
import Cookies from 'ember-cli-js-cookie';

export default Service.extend({
  authToken: Cookies.get('auth-token'),
  userID: Cookies.get('user-id')
});
