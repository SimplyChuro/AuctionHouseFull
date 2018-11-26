import Controller from '@ember/controller';

export default Controller.extend({
  session: Ember.inject.service(),

  amountHasError: null,
  amountErrorMessage: null,
  currentPicture: null,
  bidValue: null,

  sortedBids: Ember.computed('model', function(){
    return this.get('model.product.bids').sortBy('amount').reverse();
  }),

  actions: {
    createBid: function(productID) {
      var self = this;

      function transitionToHome(data) {
        self.get('target').send('refresh');
      }

      function failure(reason) {
        self.get('target').send('refresh');
      }

      let bid = this.store.createRecord('bid');
      bid.set('amount', this.get('bidAmount'));
      bid.set('product_id', productID);

      var topBid = this.get('sortedBids.firstObject.amount');
      if(topBid == null){
        topBid = this.get('model.product.startingPrice');
      }
      var userBid = this.get('bidAmount');
      
      bid.validate().then(({ validations }) => {
        if(validations.get('isValid') && (userBid > topBid) && (userBid != topBid)){
          this.set('amountHasError', false);
          bid.save().then(function() {
            self.get('target').send('refresh');
          }, function(response){
            self.get('target').send('refresh');
          });

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
      this.set('bidAmount', null);
      this.set('currentPicture', null);
      this.set('amountHasError', null);
      this.set('amountErrorMessage', null);
    }
  }
});
