import Route from '@ember/routing/route';

export default Route.extend({
	model(){
	    return Ember.RSVP.hash({
	     	categoryList: this.store.findAll('category'),
	        // productRandom: this.store.findRecord('product', 5),
	        productList: this.store.findAll('product')
	        
	   	})
	}
});
