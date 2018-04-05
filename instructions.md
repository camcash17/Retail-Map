# AWS SITE DEPLOYMENT 

## Getting Started

* Navigate to your AWS console
* Click on the Services drop-down tab
* Select S3 under Storage
* Create a new bucket 
* Name the bucket and hit next skipping all the steps until you create the bucket
* Click on the bucket and then choose properties tab 
* Select Static website hosting and then select "Use this bucket to host a website"


## Creating a New User

* Configure AWS CLI
* Navigate to AWS console on the website and choose the IAM console
* Hit next permissions and Create a user group 
* Name the group
* Once it's created click on the new group you just named
* Choose users
* Choose new user 
* Add a user name and choose Progammatic access hit next
* Save the key id and secret key access 
* Close it

## Terminal Command Line 

* Clone the project application repository (Clone this project repo (https://github.com/camcash17/Retail-Map.git)
* The Backend is labeled as "lower-manhattan-retailers" and the Frontend is labeled as "retail-map"
* At the command line type `aws configure`
* Add your keys and region:
``` 
    AWS Access Key ID [None]: **********************BQA
    AWS Secret Access Key [None]: **********************3IV
    Default region name [None]: us-east-1
    Default output format [None]: json 
```
Note: 
* npm install 
* npm run build
* Sanity check, type on command line: `aws s3 ls` It should return a list of buckets available
* Sync the folder with the bucket: `aws s3 sync build/ s3://*your bucket name*`

NOTE IF YOU GOT ACCESS ERROR:

Go to your bucket and choose overview and select upload: 
Choose upload and upload all the content from inside of your build directory

Once is done, choose properties and click on Static website hosting and you might see the link there, click on it and you will see your website hosting. 


## Update and Configure Server for Scalability 

* Navigate back to your AWS console
* From the Services dropdown tab, select EC2 and choose launch instance 
* Choose Ubuntu Server 16.04
* Choosing the free tier for Microservices is not good... So choose `T2 Large`
* Don't choose anything on step 3
* Step 4 Change to 16 GB
* Step 5 Do not add tag now
* Step 6 Configuring SSH:
    - Add a rule 'Custom TCP' 8080 (for our API gateway)
    - Add a rule 'Custom TCP' 8761 (for Eureka)
    - Add a rule 'Custom TCP' 5432 (for the database)

NOTE: Make sure every source field is set to:
0.0.0.0/0, ::/0

* Review and launch
* It's going to as for a key pair. Choose create a key pair and add `key-pair` and download it.
* Launch and see. 
* At the bottom of the screen you will see PUBLIC DNS (IPV4). Highlight and copy the DNS

## Back to Terminal Command Line 

* To precent permission problems, input:
    - chmod 400 ~/Downloads/seca-keypair.pem

* Once you've had your key-pair downloaded, input:
    - ssh -i ~/Downloads/seca-keypair.pem ubuntu@*Paste your public DNS here*

You should see something like this in your terminal:

```ubuntu@ip-172-31-50-35```

## Downloading and Setting up Docker (Ubuntu Terminal)

1. Download docker:

```curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -```

```sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"```

```sudo apt-get update```

```sudo apt-get install -y docker-ce```

2. Check what version of Ubuntu you have: `lsb_release -a`

3. Installing docker-compose

```sudo curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose```

```sudo chmod +x /usr/local/bin/docker-compose```

```docker-compose --version```

## Setting up the Backend (Ubuntu Terminal)

* Clone this project repo (https://github.com/camcash17/Retail-Map.git)
* Navigate to the wrapper directory for the backend (lower-manhattan-retailers) and type `sudo docker-compose up`

NOTE: if you run into errors, just copy the link into axios... 

## Updating the React Frontend (Normal Terminal)

* Navigate into the retail-map front end directory. Within the .env file, paste your new public DNS and save the file. It should look something like this:

REACT_APP_HOST=*Paste your public DNS here*:8080

* Back in the terminal, navigate to the retail-map directory and run the following commands:
* `npm run build`
* Sync the folder with the bucket: `aws s3 sync build/ s3://*your bucket name*`

NOTE IF YOU GOT ACCESS ERROR:

Go to your bucket and choose overview and select upload: 
Choose upload and upload all the content from inside of your build directory

Once is done, choose properties and click on Static website hosting and you might see the link there, click on it and you will be able to view the application hosting. 