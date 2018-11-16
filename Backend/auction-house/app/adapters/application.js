import DS from 'ember-data';

export default DS.RESTAdapter.extend({
	host: 'http://localhost:9000',
	namespace: 'api/v1'
});