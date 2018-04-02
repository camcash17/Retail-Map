import React, { Component } from 'react';
import {Navbar, Nav, NavItem, NavDropdown, MenuItem, Image} from 'react-bootstrap';
import Pic from './NYCOpenData_Logo.png';

class Header extends Component {
    state = {}
    render() { 
        return ( 
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="https://opendata.cityofnewyork.us/">
                            <Image className="brand" src={Pic} height={'140px'} width={'160px'} responsive />
                        </a>
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav pullRight>
                        <NavItem eventKey={1} onClick={() => this.props.resetSelect()}>
                            Map Favorites
                        </NavItem>
                        {/* <DropdownButton title="Select a Retail Type" id="dropdown-size-medium" name="primary-list" 
                        onChange={(event) => this.props.selection(event)}>
                            <MenuItem eventKey="1" value="Casual">Casual Eating & Takeout</MenuItem>
                            <MenuItem eventKey="2" value="Personal">Personal and Professional Services</MenuItem>
                            <MenuItem eventKey="3" value="Dining">Full Service Dining</MenuItem>
                            <MenuItem eventKey="3" value="Shopping">Shopping</MenuItem>
                            <MenuItem eventKey="3" value="Community">Community Resources</MenuItem>
                            <MenuItem eventKey="3" value="Nightlife">Nightlife</MenuItem>
                            <MenuItem eventKey="3" value="Visitor">Visitor Services</MenuItem>
                        </DropdownButton> */}
                        <NavItem>
                            <select id="selectId" name="primary-list" 
                            onChange={(event) => this.props.selection(event)}
                            >
                                <option value="0">Select a Retail Type</option>
                                <option value="Casual">Casual Eating & Takeout</option>
                                <option value="Personal">Personal and Professional Services</option>
                                <option value="Dining">Full Service Dining</option>
                                <option value="Shopping">Shopping</option>
                                <option value="Community">Community Resources</option>
                                <option value="Nightlife">Nightlife</option>
                                <option value="Visitor">Visitor Services</option>
                            </select>
                        </NavItem>
                    </Nav>
                </Navbar.Collapse>    
            </Navbar>
         )
    }
}
 
export default Header;