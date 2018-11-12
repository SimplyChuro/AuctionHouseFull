import DS from 'ember-data';
import { computed } from '@ember/object';
import ENV from 'auction-house/config/environment';

export default DS.RESTAdapter.extend({
	host: 'http://localhost:9000',
	namespace: 'api/v1',
 	headers: computed(function() {
    	return {
      		'X-AUTH-TOKEN': ENV.USER_TOKEN
    	};
 	})
});