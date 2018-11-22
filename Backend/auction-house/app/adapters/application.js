import DS from 'ember-data';
import { computed } from '@ember/object';
import ENV from 'auction-house/config/environment';
import config from '../config/environment';
import Cookies from 'ember-cli-js-cookie';

export default DS.RESTAdapter.extend({
  session: Ember.inject.service(),
  host: ENV.HOST_URL,
  namespace: 'api/v1',
  headers: computed(function() {
    return {
      'X-AUTH-TOKEN': this.get('session').authToken
    };
  })
});