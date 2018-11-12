import Controller from '@ember/controller';

export default Controller.extend({
	actions: {
       	createBid: function(productID) {
			this.store.createRecord('bid', {
				amount: this.get('bidAmount'),
			  	date: new Date(),
			  	status: 'active',
			  	product_id: productID
			}).save();
			// let route = Ember.getOwner(this).lookup(`route:${get(this, 'single-product/'+productID)}`);
   // 			return route.refresh();
   		}
    }
});
