import Controller from '@ember/controller';
import { later } from '@ember/runloop';

export default Controller.extend({
	actions: {
       	createBid: function(productID) {
			this.store.createRecord('bid', {
				amount: this.get('bidAmount'),
			  	date: new Date(),
			  	status: 'active',
			  	product_id: productID
			}).save();
			later(this, function() {
			 	this.get('target').send('refresh');   
            }, 200);
   		}
    }
});
