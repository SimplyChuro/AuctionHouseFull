import DS from 'ember-data';
import ENV from 'auction-house/config/environment';
import { inject as service } from '@ember/service';

export default DS.RESTAdapter.extend({
  customSession: service(),
  host: ENV.HOST_URL,
  namespace: 'api/v1',

  headers: function() {
    return {
      'X-AUTH-TOKEN': this.get('customSession').getAuthToken()
    };
  }.property().volatile()
});