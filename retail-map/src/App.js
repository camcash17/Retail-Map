import React, { Component } from 'react';
import './App.css';
import axios from 'axios';
import Infinite from 'react-infinite';
import Marker from './Marker';
import AddFavorites from './AddFavorites';
import Header from './Header';
import $ from 'jquery';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import Drawer from 'material-ui/Drawer';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import { ListGroup, ListGroupItem, DropdownButton, MenuItem, Button } from 'react-bootstrap';


class App extends Component {
  constructor(){
    super();
    this.state = {
      selected: "",
      selectedList: false,
      anchorEl: null,
      open: false
    }
    this.resetSelect = this.resetSelect.bind(this);
  }

  //method that makes a call to the database and stores the resukts in state
  componentDidMount() {
    axios.get(process.env.REACT_APP_HOST+`/retailers`)
    .then(function (response) {
      console.log('response', response.data)
      this.setState({
        retailers: response.data,
      })
    }.bind(this))
    .catch(function (error) {
      console.log("Error retrieving retail data");
      console.log(error);
    })
  }

  //method that queries the external API call to retrieve data to set as state. sets a boolean value false to clear data
  handleSecondaryChange = async (event) => {
    this.setState({
      results: null
    })
    axios.get(`https://data.cityofnewyork.us/resource/uyz2-yxi9.json?$q=${event.target.value}`)
    .then(function (response) {
      if(response.data.length<1) {
        this.viewFavorites()
      } else {
        this.setState({
          results: response.data,
          selectedList: false
        })
      }
    }.bind(this))
    .catch(function (error) {
      console.log("Error Setting Selected State");
      console.log(error);
    })
  }

  //method to to take results of the secondary category chosen and define a variable to set as state and save data necessary to add to favorites
  handlePrimaryChange = async (event) => {
    let secondaryList;
    secondaryList = this.state.results.filter((secondary, index) => {
      if(event.target.value === secondary.secondary.substring(2, secondary.secondary.length) && secondary.latitude && secondary.longitude) {
        return (
          secondary.cnbio_org_name, 
          secondary.primary,
          secondary.secondary,
          secondary.latitude,
          secondary.longitude
        )
      }
    })
    this.setState({
      selectedList: true,
      secondaryList: secondaryList
    })
  }

  //method to delete a favorite item and remove it from the DB via axios call from the array that lives in state
  deleteFavorite = async (retailerId, index) => {
    try {
      await axios.delete(process.env.REACT_APP_HOST+`/retailers/${retailerId}`);
      const updatedRetailersList = [...this.state.retailers];
      updatedRetailersList.splice(index, 1);
      this.setState({ retailers: updatedRetailersList });
    } catch (error) {
      console.log(`Error deleting Retailer with ID of ${retailerId}`);
      console.log(error);
    }
  };

  //method to add favorite and add it to the DB via axios call and push the new value to the array that lives in state
  addFavorite = async (retailer, index) => {
    this.resetSelect();
    console.log('create', retailer)
    try {
      // this.state.retailers.map((favorite, index) => {
      //   if(retailer.lat === favorite.lat)
      //     return(
            
      //     )
      // })
      const newFavoriteResponse = await axios.post(process.env.REACT_APP_HOST+`/retailers`, retailer);

      const updatedFavoriteList = [...this.state.retailers];
      updatedFavoriteList.push(newFavoriteResponse.data);
      this.setState({ 
          retailers: updatedFavoriteList,
          selectedList: false,
          results: ""
      });
    } catch (error) {
    console.log("Error creating new Favorite!");
    console.log(error);
    }
  };

  //method to patch the new favorite by making an axios call to the database
  updateFavorite = async index => {
    console.log('state', this.state.retailers[index])
    try {
      const retailerToUpdate = this.state.retailers[index];
      await axios.patch(process.env.REACT_APP_HOST+`/retailers/${retailerToUpdate.id}`, retailerToUpdate);
      console.log('Updated Favorite!');
    } catch (error) {
      console.log("Error updating Favorite!");
      console.log(error);
    }
  };

  //method to set the state of user input for favorite edit
  handleFavoriteChange = (event, index) => {
    console.log('event name', event.target.value)
    const attributeToChange = event.target.name;
    const newValue = event.target.value;

    const updatedFavoritesList = [...this.state.retailers];
    const retailerToUpdate = updatedFavoritesList[index];
    retailerToUpdate[attributeToChange] = newValue;

    this.setState({ retailers: updatedFavoritesList });
  };

  viewFavorites() {
    $('#selectId').val('0');
    this.setState({
      selectedList: false,
      results: ""
    })
  }

  resetSelect() {
    $('#selectId').val('0');
    this.setState({
      selectedList: false,
      results: ""
    })
  }

  handleToggle = () => this.setState({open: !this.state.open});

  handleClose = () => this.setState({open: false});

  render() {
    //conditional statements to filter secondary category names from external API data and then remove duplicates
    let secondaries;
    let secondariesList;
    if(this.state.results) {
      secondaries = this.state.results.map((secondary, i) => {
        return (
          secondary.secondary.substring(2, secondary.secondary.length)
        )
      })
      secondariesList = secondaries.filter((val, id, array) => {
        return array.indexOf(val) === id;  
      })
    }

    return (
      <div className="App">
        <Header selection={this.handleSecondaryChange} resetSelect={this.resetSelect}  />
        <div className="header">
          <h1 style={{padding: '15px', color: 'white'}}>Lower Manhattan Retail Map</h1>
        </div>
        <br />
          <div className="layout">
            <div id="one">
              <h1 className="header" style={{margin: 0, padding: '10px', color: 'white', fontSize: '33px'}}>Retail Locations</h1>
              {this.state.results ?
              <div>
                <select name="secondary-list" 
                  onChange={(event) => this.handlePrimaryChange(event)}
                  style={{margin: '10px'}}
                  >
                  <option>Select a Category</option>
                  {secondariesList.map((secondary, i) => {
                    return (
                      <option key={i} value={secondary} style={{margin: '10px', padding: '5px'}}>{secondary}</option>
                    )
                  })}
                </select>
              </div>
              : null}
              {this.state.selectedList ?
                <Infinite containerHeight={500} elementHeight={40}>
                  <div>
                    {this.state.secondaryList.map((selected, i) => {
                      return (
                        selected ? 
                        <ListGroup key={i}>
                          <ListGroupItem bsStyle="info">
                            <div>
                              <h4>{selected.cnbio_org_name}</h4>
                              <p>{selected.cnadrprf_addrline1}</p>
                              <AddFavorites  
                                selected={selected}
                                retailers={this.state.retailers}
                                addFavorite={this.addFavorite}
                                />
                            </div> 
                          </ListGroupItem>
                        </ListGroup>
                        : null
                      )
                    })} 
                  </div>
                </Infinite>
              : null}
             </div>
             <div id="two">
              {this.state.retailers ?
              <Marker selected={this.state.secondaryList} favorites={this.state.retailers} selectedList={this.state.selectedList} /> 
              : null }
             </div>
            <div id="three">
              <h1 className="header" style={{margin: 0, marginBottom: '20px', padding: '10px', color: 'white', fontSize: '33px'}}>Favorites</h1>
              {this.state.retailers ?
              <Infinite containerHeight={500} elementHeight={40}>
                <div>
                  {this.state.retailers.map((favorites, index) => {
                    return (
                    <ListGroup key={index}>
                      <ListGroupItem bsStyle="info">
                      <div>
                        <div>
                            <input
                              name="orgName"
                              onChange={(event) => this.handleFavoriteChange(event, index)}
                              onBlur={() => this.updateFavorite(index)}
                              value={favorites.orgName} />
                        </div>
                        <MuiThemeProvider>
                          <div>
                            <RaisedButton
                              label="Open Drawer"
                              onClick={this.handleToggle}
                            />
                            <Drawer
                              docked={false}
                              width={200}
                              open={this.state.open}
                              onRequestChange={(open) => this.setState({open})}
                            >
                              <MenuItem onClick={this.handleClose}>Menu Item</MenuItem>
                              <MenuItem onClick={this.handleClose}>Menu Item 2</MenuItem>
                            </Drawer>
                          </div>
                        </MuiThemeProvider>
                        <Button className="buttonOne" style={{marginTop: '5px'}} onClick={() => this.deleteFavorite(favorites.id, index)}>
                          Delete
                        </Button>
                      </div>
                      </ListGroupItem>
                    </ListGroup>
                    )
                  })}
                </div>
              </Infinite>
              : null }
            </div>
          </div>
      </div>
    ) 
  }
}

export default App;
