import Controller from '@ember/controller';
import { later } from '@ember/runloop';

export default Controller.extend({
  session: Ember.inject.service(),
  currentPicture: null,
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
    },
    createWishlist: function(productID) {
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save();
      later(this, function() {
        this.get('target').send('refresh');
      }, 200);
    },
    setCurrentPicture: function(picture){
      this.set('currentPicture', picture);
    },
    clearFields: function(){
      this.set('bidAmount', '');
      this.set('currentPicture', null);
    }
  }
});
