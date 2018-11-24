import Controller from '@ember/controller';
import { later } from '@ember/runloop';

export default Controller.extend({
  session: Ember.inject.service(),

  amountHasError: null,
  amountErrorMessage: null,
  currentPicture: null,

  sortedBids: Ember.computed(function(){
    return this.get('model.product.bids').sortBy('amount').reverse();
  }),

  actions: {
    createBid: function(productID) {
      var self = this;

      function transitionToHome(data) {
        console.log(data);
        self.get('target').send('refresh');
      }

      function failure(reason) {
        // self.get('target').send('refresh');
      }

      let bid = this.store.createRecord('bid');
      bid.set('amount', this.get('bidAmount'));
      bid.set('product_id', productID);

      bid.validate().then(({ validations }) =>{
        if(validations.get('isValid')){
          bid.save().then(transitionToHome).catch(failure);

        } else {

          if(bid.get('validations.attrs.amount.messages') !== '' && bid.get('validations.attrs.amount.messages') !== null){
            this.set('amountHasError', true);
            this.set('amountErrorMessage', bid.get('validations.attrs.amount.messages'));
          }

        }
      })
    },

    createWishlist: function(productID) {
      this.store.createRecord('wishlist', {
        product_id: productID
      }).save();  
    },

    setCurrentPicture: function(picture){
      this.set('currentPicture', picture);
    },

    clearFields: function(){
      this.set('bidAmount', '');
      this.set('currentPicture', null);
      this.set('amountHasError', null);
      this.set('amountErrorMessage', null);
    }
  }
});
