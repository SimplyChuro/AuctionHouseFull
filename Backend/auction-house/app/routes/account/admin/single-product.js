import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(params) {
    return hash({
      product: this.store.findRecord('product', params.product_id),
      categoryList: this.store.findAll('category')
    })
  },

  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/admin/single-product').set('nameInput', model.product.name);
    this.controllerFor('account/admin/single-product').set('descriptionInput', model.product.description);
    this.controllerFor('account/admin/single-product').set('startingPriceInput', model.product.startingPrice);
    this.controllerFor('account/admin/single-product').set('startDateInput', model.product.publishDate);
    this.controllerFor('account/admin/single-product').set('endDateInput', model.product.expireDate);
    
  },
 
  actions: { 
    willTransition: function() {
      this.controllerFor('account/admin/single-product').send('clearFields');
    }
  }
});
