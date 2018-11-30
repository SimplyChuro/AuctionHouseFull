import Controller from '@ember/controller';

export default Controller.extend({
  session: Ember.inject.service(),

  amountHasError: null,
  amountErrorMessage: null,
  currentPicture: null,
  bidValue: null,
  bidListSize: 5,

  sortedBids: Ember.computed('model.product.bids.[]', function(){
    return this.get('model.product.bids').sortBy('amount').reverse();
  }),

  userWishlistItem: Ember.computed('model.wishlist.[]', function(){
    var _this = this;

    var wishlistItems = this.get('model.wishlist');
    var checker = null;
    wishlistItems.forEach(function(item){
      if(checker == null) {
        if(_this.get('model.product.id') == item.get('product').get('id')) {
          checker = item;
        }
      }
    });

    return checker;
  }),

  actions: {

    createBid: function(productID) {
      var _this = this;

      function transitionToHome(data) {
        _this.get('target').send('refresh');
      }

      function failure(reason) {
        _this.get('target').send('refresh');
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
            _this.get('flashMessages').success('Bid Created!');
            _this.get('target').send('refresh');
          }, function(response){
            // _this.get('flashMessages').failure('An error occoured please try again!');
          });

        } else {

          if(bid.get('validations.attrs.amount.messages') !== '' && bid.get('validations.attrs.amount.messages') !== null){
            _this.set('amountHasError', true);
            _this.set('amountErrorMessage', bid.get('validations.attrs.amount.messages'));
          }

        }
      })
    },


    createWishlist: function(productID) {
      var _this = this;
      this.store.createRecord('wishlist', {
        product_id: productID
      }).save().then(function(){
        _this.get('target').send('refresh');       
      }).catch(function(){
      });  
    },


    deleteWishlist: function(productID) {
      var _this = this;
      var wishlist = _this.get('userWishlistItem');
      wishlist.destroyRecord();
    },

    setCurrentPicture: function(picture){
      this.set('currentPicture', picture);
    },

    loadMore: function(){
      this.set('bidListSize', this.get('bidListSize') + 5);
    },

    clearFields: function(){
      this.set('bidAmount', null);
      this.set('currentPicture', null);
      this.set('amountHasError', null);
      this.set('amountErrorMessage', null);
      this.set('bidListSize', 5);
    }
  }
});
