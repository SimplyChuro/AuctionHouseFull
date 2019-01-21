import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(params) {
    return hash({
      product: this.store.findRecord('product', params.product_id)
    })
  },
 
  actions: {
    refresh: function() {
      this.refresh();
    },
    
    willTransition: function() {
      this.controllerFor('shop/single-product').send('clearFields');
    }
  }
});
