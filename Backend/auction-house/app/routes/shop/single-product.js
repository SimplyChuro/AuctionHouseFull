import Route from '@ember/routing/route';

export default Route.extend({
	model(params) {
		return this.store.findRecord('product', params.product_id);
  	},
  	actions: {
       	createBid: function(productID) {
			this.store.createRecord('bid', {
				amount: this.get('controller').get('bidAmount'),
			  	date: new Date(),
			  	status: 'active',
			  	product_id: productID
			}).save();
			this.refresh();
   		}
    }
});
