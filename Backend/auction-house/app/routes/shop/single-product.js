import Route from '@ember/routing/route';

export default Route.extend({
  model(params) {
    return Ember.RSVP.hash({
      product: this.store.findRecord('product', params.product_id, {reload: true}),
    })
  },

  actions: {
    refresh: function() {
      this.refresh();
    },
    willTransition: function(transition) {
      this.controllerFor('shop/single-product').send('clearFields');
    }
  }
});
