import Route from '@ember/routing/route';

export default Route.extend({
	model(){
	    return Ember.RSVP.hash({
	     	saleList: this.store.findAll('sale')
	   	})
	}
});
