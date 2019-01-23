import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(params) {
    return hash({
      categoryList: this.store.findAll('category'),
      saleItem: this.store.findRecord('sale', params.sale_id, { reload: true }),
    })
  },

  setupController(controller, model) {
    this._super(controller, model);
    this.controllerFor('account/sell/sale-item').set('nameInput', model.saleItem.product.get('name'));
    this.controllerFor('account/sell/sale-item').set('descriptionInput', model.saleItem.product.get('description'));
    this.controllerFor('account/sell/sale-item').set('colorInput', model.saleItem.product.get('color'));
    this.controllerFor('account/sell/sale-item').set('sizeInput', model.saleItem.product.get('size'));

    model.saleItem.product.get('productcategory').forEach((item, index) => {
      if(item.category.get('parent_id') == null){
        this.controllerFor('account/sell/sale-item').set('category', item.category.get('id'));
      } else {
        this.controllerFor('account/sell/sale-item').set('subCategory', item.category.get('id'));
      }
    });

    this.controllerFor('account/sell/sale-item').set('startingPriceInput', model.saleItem.product.get('startingPrice'));
    this.controllerFor('account/sell/sale-item').set('startDateInput', model.saleItem.product.get('publishDate'));
    this.controllerFor('account/sell/sale-item').set('endDateInput', model.saleItem.product.get('expireDate'));
    this.controllerFor('account/sell/sale-item').set('addressInput', model.saleItem.get('address'));
    this.controllerFor('account/sell/sale-item').set('cityInput', model.saleItem.get('city'));
    this.controllerFor('account/sell/sale-item').set('countryInput', model.saleItem.get('country'));
    this.controllerFor('account/sell/sale-item').set('zipCodeInput', model.saleItem.get('zipCode'));
    this.controllerFor('account/sell/sale-item').set('phoneInput', model.saleItem.get('phone'));
    this.controllerFor('account/sell/sale-item').set('token', model.saleItem.get('paymentToken'));
  },

  actions: { 
    willTransition: function() {
      this.controllerFor('account/sell/sale-item').send('clearFields');
    }
  }
});