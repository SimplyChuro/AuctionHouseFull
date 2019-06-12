import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { hash } from 'rsvp';

export default Route.extend({
  customSession: service(),

  model(params) {
    return hash({
      product: this.store.findRecord('product', params.product_id),
      user: this.store.findRecord('user', this.get('customSession').getUserID(), { reload: true }),
    })
  },
 
  actions: { 
    willTransition: function() {
      this.controllerFor('shop/product-payment').send('clearFields');
    }
  }

});
